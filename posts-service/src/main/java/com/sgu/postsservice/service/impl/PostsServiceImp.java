package com.sgu.postsservice.service.impl;

import com.sgu.postsservice.constant.Constant;
import com.sgu.postsservice.constant.PostStatus;
import com.sgu.postsservice.dto.request.*;
import com.sgu.postsservice.dto.response.HttpResponseEntity;
import com.sgu.postsservice.dto.response.Pagination;
import com.sgu.postsservice.dto.response.PostsResponse;
import com.sgu.postsservice.exception.BadRequestException;
import com.sgu.postsservice.exception.InternalServerException;
import com.sgu.postsservice.exception.NotFoundException;
import com.sgu.postsservice.exception.ServerInternalException;
import com.sgu.postsservice.model.Category;
import com.sgu.postsservice.model.PostImage;
import com.sgu.postsservice.model.Posts;
import com.sgu.postsservice.repository.CategoryRepository;
import com.sgu.postsservice.repository.PostImageRepository;
import com.sgu.postsservice.repository.PostsRepository;
import com.sgu.postsservice.service.CloudinaryService;
import com.sgu.postsservice.service.PostsService;
import com.sgu.postsservice.utils.DateUtils;
import com.sgu.postsservice.utils.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostsServiceImp implements PostsService {
    @Autowired
    private WebClient webClient;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CloudinaryService cloudinaryService;


    @Override
    public HttpResponseEntity createPosts(PostRequest postRequest) {
        HttpResponseEntity getAccountByid = null;
        try{
            getAccountByid = webClient.get()
                    .uri("http://localhost:8082/api/v1/account/get-account-by-person-id/"+postRequest.getAccountId())
                    .retrieve()
                    .bodyToMono(HttpResponseEntity.class)
                    .block();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            throw new NotFoundException("Tài khoản không tồn tại");
        }

        Category category = categoryRepository.findById(postRequest.getCategoryId()).orElseThrow(
                ()-> new NotFoundException(String.format("Danh mục bài viết id=%s không tồn tại",postRequest.getCategoryId()))
        );

        Posts postsEntity = convertToEntity(postRequest);
        postsEntity.setCategory(category);
        postsEntity.getPostImageList().forEach(item->item.setPosts(postsEntity));

        postsRepository.save(postsEntity);
        List<PostsResponse> postsList = Arrays.asList(this.convertToResponse(postsEntity));

        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.CREATED.value(),
                Constant.SUCCESS,
                postsList,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getAll() {
        List<Posts> postsList = postsRepository.findAll();
        List<PostsResponse> postsResponseList = postsList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.CREATED.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getAllAvailablePosts() {
        List<Posts> postsList = postsRepository.findAll()
                .stream()
                .filter(posts->posts.getPostStatus().equals(PostStatus.DISPLAY))
                .collect(Collectors.toList());
        List<PostsResponse> postsResponseList = postsList.stream().map(this::convertToResponse).collect(Collectors.toList());
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.CREATED.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getAllAvailablePostsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        Page<Posts> postsPage = postsRepository.findAll(pageable);
        List<PostsResponse> postsList = postsPage.getContent()
                .stream()
                .filter(posts->posts.getPostStatus().equals(PostStatus.DISPLAY))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        Pagination pagination = Pagination.builder()
                .page(page)
                .size(size)
                .total_page(postsPage.getTotalPages())
                .total_size(postsPage.getTotalElements())
                .build();
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsList,
                pagination
        );
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getAllWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        Page<Posts> postsPage = postsRepository.findAll(pageable);
        Pagination pagination = Pagination.builder()
                .page(page)
                .size(size)
                .total_page(postsPage.getTotalPages())
                .total_size(postsPage.getTotalElements())
                .build();

        List<PostsResponse> postsResponseList = postsPage.getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                pagination
        );
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity searchByKeyWord(String keyword) {
        List<String> keywordList = Arrays.asList(keyword.split(" "))
                .stream()
                .map(str->StringUtils.convertTextToEnglish(str))
                .collect(Collectors.toList());
        List<Posts> postsList = postsRepository.findAll()
                .stream()
                .filter(posts->posts.getPostStatus().equals(PostStatus.DISPLAY))
                .collect(Collectors.toList());

        Collections.sort(postsList,(posts1,post2)->{
            return posts1.compareTitle(post2,keywordList);
        });

        List<PostsResponse> postsResponseList = postsList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity searchMultipleCondition(
            Long categoryId, Boolean isNewest, String keyword, Integer page, Integer size) {

        List<Posts> postsList = postsRepository.findAll()
                .stream()
                .filter(posts->posts.getPostStatus().equals(PostStatus.DISPLAY))
                .collect(Collectors.toList());
        //Filler category id
        if(categoryId != null){
            postsList = postsList.stream()
                    .filter(posts -> posts.getCategory().getId() == categoryId)
                    .collect(Collectors.toList());
        }

        //divide post base on priority
        if(keyword != null) {
            List<String> keywordList = Arrays.asList(keyword.split(" "))
                    .stream()
                    .map(str->StringUtils.convertTextToEnglish(str))
                    .collect(Collectors.toList());
            TreeMap<Integer, List<Posts>> treeMap = new TreeMap<>(Collections.reverseOrder());
            for (Posts p : postsList) {
                int priority = Posts.calcPriority(p.getTitle(), keywordList);

                if (treeMap.containsKey(priority)) {
                    List<Posts> treeListData = new ArrayList<>(treeMap.get(priority));
                    treeListData.add(p);
                    treeMap.put(priority, treeListData);
                } else {
                    treeMap.put(priority, Arrays.asList(p));
                }
            }

            //Sort each element in treemap
            for(Map.Entry<Integer, List<Posts>> entry : treeMap.entrySet()){
                List<Posts> postsListTreeMapItem = new ArrayList<>(entry.getValue());
                Collections.sort(postsListTreeMapItem,(p1,p2)->{
                    return p1.compareTime(p2,isNewest);
                });
                treeMap.put(entry.getKey(), postsListTreeMapItem);
            }

            //Convert treemap to arraylist
            List<Posts> sortedList = new ArrayList<>();

            for(Map.Entry<Integer, List<Posts>> entry : treeMap.entrySet()){
                sortedList.addAll(new ArrayList<>(entry.getValue()));
            }

            postsList = sortedList;
        }
        else{
            Collections.sort(postsList,(p1,p2)->{
                return p1.compareTime(p2,isNewest);
            });
        }

        //Pagination
        int to = page * size;
        int from = to - size;
        int totalPage = postsList.size() % size == 0 ?
                postsList.size()/size: postsList.size()/size+1;

        List<Posts> paginationList = new ArrayList<>();
        for(int i = from;i < to;i++){
            if(i < postsList.size()){
                paginationList.add(postsList.get(i));
            }
        }

        List<PostsResponse> postsResponseList = paginationList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());


        Pagination pagination = Pagination.builder()
                .page(page)
                .size(size)
                .total_page(totalPage)
                .total_size(postsList.size())
                .build();


        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                pagination
        );
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity updatePosts(Long id, PostUpdateRequest postUpdateRequest) {
        Posts posts = postsRepository.findById(id).orElseThrow(
                ()->new NotFoundException(String.format("Không tìm thấy bài viết có id=%s",id))
        );

        Category category = categoryRepository.findById(postUpdateRequest.getCategoryId()).orElseThrow(
                ()->new NotFoundException(String
                        .format("Danh mục với id=%s không tồn tại",postUpdateRequest.getCategoryId()))
        );

        //remove old image
        postImageRepository.deleteAll(posts.getPostImageList());
        posts.setCategory(category);
        posts.setTitle(postUpdateRequest.getTitle());
        posts.setBody(postUpdateRequest.getBody());
        posts.setPrice(postUpdateRequest.getPrice());
        posts.setPostImageList(postUpdateRequest.getImageList()
                .stream()
                .map(imageRequest-> PostImage.builder()
                        .url(imageRequest.getUrl())
                        .posts(posts)
                        .build())
                .collect(Collectors.toList()));
        List<PostsResponse> updatedPost = Arrays.asList(this.convertToResponse(posts));
        postsRepository.save(posts);
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                updatedPost,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity uploadMultiFiles(MultipartFile[] files) {
        List<String> list = new ArrayList<>();
        for(MultipartFile file : files){
            try{
                String contentType = file.getContentType();
                Predicate<String> checkFileExtension = s -> {
                    return !contentType.equals("image/jpeg") && !contentType.equals("image/png");
                };
                if(checkFileExtension.test(contentType)){
                    throw new BadRequestException(String.format("Chỉ chấp nhận file png,jpg,jpeg"));
                }

                Map<?,?> map = cloudinaryService.upload(file,"posts/");
                list.add((String)map.get("url"));

            }catch (InternalServerException e) {
                throw new ServerInternalException(String.format("Lỗi khi upload nhiều file %s",e.getMessage()));
            }
        }
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                list,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getById(Long id) {
        Posts posts  =postsRepository.findById(id).orElseThrow(
                ()->new NotFoundException(String.format("Bài viết với id=%s không tồn tại",id))
        );
        PostsResponse postsResponse = this.convertToResponse(posts);
        List<PostsResponse> postsResponseList = Arrays.asList(postsResponse);

        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getByAccountId(Long id) {
        List<Posts> postsList = postsRepository.getByAccountId(id);
        List<PostsResponse> postsResponseList = postsList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getByCategoryId(Long id) {
        List<Posts> postsList = postsRepository.getByCategoryId(id);
        List<PostsResponse> postsResponseList = postsList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity userHidden(PostsHiddenRequest postsHiddenRequest) {
        Posts posts = postsRepository.findById(postsHiddenRequest.getPostsId())
                .orElseThrow(()->new NotFoundException(String
                        .format("Bài viết với id=%s không tồn tại", postsHiddenRequest.getPostsId())));


        posts.setPostStatus(PostStatus.USER_HIDDEN);
        posts = postsRepository.save(posts);
        List<PostsResponse> postsResponseList = Arrays.asList(this.convertToResponse(posts));
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity userUnhidden(PostsHiddenRequest postsHiddenRequest) {
        Posts posts = postsRepository.findById(postsHiddenRequest.getPostsId())
                .orElseThrow(()->new NotFoundException(String
                        .format("Bài viết với id=%s không tồn tại", postsHiddenRequest.getPostsId())));

        posts.setPostStatus(PostStatus.DISPLAY);
        posts = postsRepository.save(posts);
        List<PostsResponse> postsResponseList = Arrays.asList(this.convertToResponse(posts));
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity userDelete(PostsDeleteRequest postsDeleteRequest) {
        Posts posts = postsRepository.findById(postsDeleteRequest.getPostsId())
                .orElseThrow(()->new NotFoundException(String
                        .format("Bài viết với id=%s không tồn tại", postsDeleteRequest.getPostsId())));

        posts.setPostStatus(PostStatus.USER_DELETE);
        posts.setReasonBlock(postsDeleteRequest.getReasonDelete());
        posts = postsRepository.save(posts);
        List<PostsResponse> postsResponseList = Arrays.asList(this.convertToResponse(posts));
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity adminDelete(PostsDeleteRequest postsDeleteRequest) {
        Posts posts = postsRepository.findById(postsDeleteRequest.getPostsId())
                .orElseThrow(()->new NotFoundException(String
                        .format("Bài viết với id=%s không tồn tại", postsDeleteRequest.getPostsId())));

        posts.setPostStatus(PostStatus.ADMIN_DELETE);
        posts.setReasonBlock(postsDeleteRequest.getReasonDelete());
        posts = postsRepository.save(posts);
        List<PostsResponse> postsResponseList = Arrays.asList(this.convertToResponse(posts));
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );

        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getByCategorySlug(String slug, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(()->new NotFoundException(
                        String.format("Không thể tìm thấy slug=%s",slug)));
        Page<Posts> postsPage = postsRepository.findAllByCategoryId(category.getId(),pageable);
        List<PostsResponse> postsResponseList = postsPage.getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .page(page)
                .size(size)
                .total_page(postsPage.getTotalPages())
                .total_size(postsPage.getTotalElements())
                .build();
        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                pagination
        );
        return httpResponseEntity;
    }

    @Override
    public HttpResponseEntity getBySlug(String slug) {
        Posts posts = postsRepository.getBySlug(slug).orElseThrow(
                ()-> new NotFoundException(String.format("Bài viết với slug=%s không tồn tại",slug))
        );

        List<PostsResponse> postsResponseList = Arrays.asList(this.convertToResponse(posts));

        HttpResponseEntity httpResponseEntity = HttpResponseEntity.convertToResponeEntity(
                HttpStatus.OK.value(),
                Constant.SUCCESS,
                postsResponseList,
                null
        );
        return httpResponseEntity;

    }

    private int calcPriority(String title, List<String> keywordList) {
        int priority = 0;
        String encodeTitle = StringUtils.convertTextToEnglish(title);
        for(String key: keywordList){
            if(encodeTitle.contains(key)){
                priority++;
            }
        }

        return priority;
    }

    private PostsResponse convertToResponse(Posts posts) {
        Category category = posts.getCategory();
        PostsResponse postsResponse = PostsResponse.builder()
                .id(posts.getId())
                .accountId(posts.getAccountId())
                .title(posts.getTitle())
                .body(posts.getBody())
                .price(posts.getPrice())
                .postsSlug(posts.getPostsSlug())
                .postStatus(posts.getPostStatus())
                .reasonBlock(posts.getReasonBlock())
                .thumbnail(posts.getThumbnail())
                .category(Category.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .url(category.getUrl())
                        .categorySlug(category.getCategorySlug())
                        .createdAt(category.getCreatedAt())
                        .updatedAt(category.getUpdatedAt())
                        .build()
                )
                .imageList(posts.getPostImageList()
                        .stream()
                        .map(item->item.getUrl())
                        .collect(Collectors.toList()))
                .createdAt(posts.getCreatedAt())
                .updatedAt(posts.getUpdatedAt())
                .build();

        return postsResponse;
    }

    private Posts convertToEntity(PostRequest postRequest) {
        Posts posts = new Posts();

        return Posts.builder()
                .accountId(postRequest.getAccountId())
                .title(postRequest.getTitle())
                .body(postRequest.getBody())
                .price(postRequest.getPrice())
                .postImageList(postRequest.getImageList().stream()
                        .map(this::convertToPostImage)
                        .collect(Collectors.toList()))
                .reasonBlock("")
                .thumbnail(postRequest.getImageList().get(0).getUrl())
                .postsSlug(StringUtils.createSlug(postRequest.getTitle() + "-" + DateUtils.getNow()))
                .build();
    }

    private PostImage convertToPostImage(ImageRequest imageRequest) {
        return PostImage.builder()
                .url(imageRequest.getUrl())
                .createdAt(DateUtils.getNow())
                .updatedAt(DateUtils.getNow())
                .build();
    }
}
