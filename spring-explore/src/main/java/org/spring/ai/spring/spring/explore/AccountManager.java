package org.spring.ai.spring.spring.explore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountManager {
    private final Map<String, Account> accounts;
    private final ScheduledExecutorService scheduler;
    private final long resetTime; // 请求重置时间

    public AccountManager(Map<String, Account> accounts, long resetTime) {
        this.accounts = accounts;
        this.resetTime = resetTime;
        this.scheduler = Executors.newScheduledThreadPool(1);
        startRequestResetScheduler();
    }

    public synchronized String getAvailableAccount() {
        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            Account account = entry.getValue();
            if (account.getRemainingRequests() > 0) {
                account.use();
                return entry.getKey();
            }
        }
        return null; // 所有账号用完
    }

    public void resetRequests() {
        for (Account account : accounts.values()) {
            account.reset();
        }
    }

    public void startRequestResetScheduler() {
        scheduler.scheduleAtFixedRate(this::resetRequests, resetTime, resetTime, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        Map<String, Account> accounts = new HashMap<>();
        accounts.put("account1", new Account(5));
        accounts.put("account2", new Account(5));
        accounts.put("account3", new Account(5));

        AccountManager manager = new AccountManager(accounts, 10);

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 15; i++) {
            executor.submit(() -> {
                String account = manager.getAvailableAccount();
                if (account != null) {
                    System.out.println("Using account: " + account);
                } else {
                    System.out.println("All accounts exhausted. Waiting...");
                    try {
                        Thread.sleep(1000); // 等待1秒后再试
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        executor.shutdown();
    }
}

class Account {
    private final AtomicInteger remainingRequests;

    public Account(int initialRequests) {
        this.remainingRequests = new AtomicInteger(initialRequests);
    }

    public void use() {
        remainingRequests.decrementAndGet();
    }

    public void reset() {
        remainingRequests.set(5);
    }

    public int getRemainingRequests() {
        return remainingRequests.get();
    }
}

