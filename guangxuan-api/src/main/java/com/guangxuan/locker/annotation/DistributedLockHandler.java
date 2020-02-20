package com.guangxuan.locker.annotation;

import com.guangxuan.dto.Result;
import com.guangxuan.exception.BusinessException;
import com.guangxuan.locker.DistributedLocker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description Created by zeng.yubo on 2019/8/13.
 */
@Aspect
@Component
public class DistributedLockHandler {

    @Autowired
    DistributedLocker distributedLocker;

    @Around("@annotation(lock)")
    public Result<?> around(ProceedingJoinPoint joinPoint, Lock lock) {
        String lockName = lock.value();
        int lessTime = lock.leaseTime();
        Object o = null;
        //上锁
        distributedLocker.lock(lockName, lessTime);
        try {
            return (Result<?>) joinPoint.proceed();
        } catch (BusinessException exception) {
            return Result.fail(exception.getResultEnum());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            //如果该线程还持有该锁，那么释放该锁。如果该线程不持有该锁，说明该线程的锁已到过期时间，自动释放锁
            System.out.println("释放");
            if (distributedLocker.isHeldByCurrentThread(lockName)) {
                distributedLocker.unlock(lockName);
            }
        }
        return null;
    }
}
