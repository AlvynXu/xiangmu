//package com.guangxuan.controller.admin;
//
//import com.guangxuan.model.MallHomeVideo;
//import com.guangxuan.service.MallHomeVideoService;
//import com.guangxuan.vo.BaseResponse;
//import com.guangxuan.vo.ErrorResponse;
//import com.guangxuan.vo.SuccessResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.CollectionUtils;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
///**
// * @author deofly
// * @since 2019-04-28
// */
//@RestController("AdminMallHomeVideoController")
//@RequestMapping("/admin/mall/home/videos")
//public class AdminMallHomeVideoController {
//
//    @Autowired
//    private MallHomeVideoService mallHomeVideoService;
//
//    @GetMapping("")
//    public BaseResponse getAllVideos() {
//        List<MallHomeVideo> videos = mallHomeVideoService.list();
//        if (CollectionUtils.isEmpty(videos)) {
//            return new SuccessResponse();
//        }
//
//        return new SuccessResponse(videos);
//    }
//
//    @GetMapping("{id}")
//    public BaseResponse getVideo(@PathVariable int id) {
//        MallHomeVideo videoDO = mallHomeVideoService.getById(id);
//        if (videoDO == null) {
//            return new ErrorResponse("该视频不存在");
//        }
//
//        return new SuccessResponse(videoDO);
//    }
//
//    @PostMapping("")
//    public BaseResponse createVideo(@RequestBody @Valid MallHomeVideo videoDO,
//                                     BindingResult result) {
//        if (result.hasErrors()) {
//            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
//        }
//
//        mallHomeVideoService.save(videoDO);
//
//        return new SuccessResponse();
//    }
//
//    @PutMapping("")
//    public BaseResponse updateVideo(@RequestBody @Valid MallHomeVideo videoDO,
//                                     BindingResult result) {
//        if (result.hasErrors()) {
//            return new ErrorResponse(result.getAllErrors().get(0).getDefaultMessage());
//        }
//
//        mallHomeVideoService.updateById(videoDO);
//
//        return new SuccessResponse();
//    }
//
//    @DeleteMapping("{id}")
//    public BaseResponse deleteVideo(@PathVariable int id) {
//        mallHomeVideoService.removeById(id);
//
//        return new SuccessResponse();
//    }
//}
