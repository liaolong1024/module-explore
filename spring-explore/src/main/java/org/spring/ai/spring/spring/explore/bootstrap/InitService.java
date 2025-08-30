package org.spring.ai.spring.spring.explore.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.spring.ai.spring.spring.explore.validator.UniqueUserValidator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author ll
 * @since 2024-11-02 11:13
 */
@Service
@Slf4j
public class InitService implements ApplicationListener<ApplicationContextEvent> {
    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        ApplicationContext context = event.getApplicationContext();
        try {
            UniqueUserValidator bean = context.getBean(UniqueUserValidator.class);
            log.info("{}", bean.getClass());
        } catch (BeansException e) {
            log.warn("bean is not exist");
        }
    }

    public static void main(String[] args) {
        new Solution().countFairPairs(new int[] {0,1,7,4,4,5}, 3, 6);
    }

    static class Solution {
        public long countFairPairs(int[] nums, int lower, int upper) {
            if (lower > upper) {
                return 0;
            }
            Optional<Object> reduce = new ArrayList<>().stream().reduce(Integer::sum);
            int n = nums.length;
            long ans = 0L;
            Arrays.sort(nums);
            for (int i = 0; i < n - 1; i++) {
                int lowerT = lower - nums[i];
                int upperT = upper - nums[i];
                int upperPos = findPos(nums, i + 1, n, upperT);
                while (upperPos < n && upperT >= nums[upperPos]) {
                    upperPos++;
                }
                int lowerPos = findPos(nums, i + 1, n, lowerT);
                while (lowerPos < n && lowerPos >= i + 1 && lowerT <= nums[lowerPos]) {
                    lowerPos--;
                }
                if (upperPos > lowerPos) {
                    ans += upperPos - lowerPos - 1;
                }
            }
            return ans;
        }

        public int findPos(int[] nums, int from, int to, int key) {
            int res = Arrays.binarySearch(nums, from, to, key);
            if (res >= 0) {
                return res;
            } else {
                return -res - 1;
            }
        }
    }
}
