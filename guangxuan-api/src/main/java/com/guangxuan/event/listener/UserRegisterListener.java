//package com.guangxuan.event.listener;
//
//import com.guangxuan.constant.RedisConstant;
//import com.guangxuan.dto.PromoterDTO;
//import com.guangxuan.event.UserRegisterEvent;
//import com.guangxuan.model.Users;
//import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
//import org.springframework.context.ApplicationListener;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @author zhuolin
// * @Date 2019/11/25
// */
//@Component
//public class UserRegisterListener implements ApplicationListener<UserRegisterEvent> {
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    @Override
//    public void onApplicationEvent(UserRegisterEvent userRegisterEvent) {
//        Users user = userRegisterEvent.getUser();
//        PromoterDTO promoterDTO = new PromoterDTO();
//        promoterDTO.setDirectCount(0);
//        promoterDTO.setIndirectCount(0);
//        promoterDTO.setPromoterId(user.getPromoterId());
//        redisTemplate.opsForValue().set(RedisConstant.USER_PROMOTE_COUNT + user.getId(), promoterDTO);
//        addTeam(promoterDTO.getPromoterId());
//    }
//
//    private void addTeam(Long userId) {
//        if (userId == null) {
//            return;
//        }
//        PromoterDTO promoterDTO = (PromoterDTO) redisTemplate.opsForValue().get(RedisConstant.USER_PROMOTE_COUNT + userId);
//        promoterDTO.setIndirectCount(promoterDTO.getIndirectCount() + 1);
//        promoterDTO.setDirectCount(promoterDTO.getDirectCount() + 1);
//        redisTemplate.opsForValue().set(RedisConstant.USER_PROMOTE_COUNT + userId, promoterDTO);
//        addTeam(promoterDTO.getPromoterId());
//    }
//}
