package com.oqplanner.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired BatchConfiguration batchConfiguration;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Scheduled(cron = "0 50 23 * * ?") // 매일 11시 50분에 실행
//    @Scheduled(cron = "0,30 * * * * ?") // 매분 0,30초에 실행
    // 혹시 배치 스케쥴러가 writer가 안돈다면 데이터 문제이다.
    // 일배치 기준으로 어제 날짜의 TRIP_FAVORITES_HISTORY의 데이터를 가져와서
    // INSERT 해주는데 오늘 기준으로 어제 날짜 데이터가 없으면 아무것도 안써주는게 맞다..
    public void runDayJob() throws Exception {

        System.out.println("Scheduled day job is running at: " + new Date());
        String tripFavoritesHistoryRegDt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        System.out.println("tripFavoritesHistoryRegDt >>> " + tripFavoritesHistoryRegDt);
        JobParameters params = new JobParametersBuilder()
                .addString("myJob", String.valueOf(System.currentTimeMillis()))
                .addString("period", "day")
                .addString("tripFavoritesHistoryRegDt ", tripFavoritesHistoryRegDt)
                .toJobParameters();


        JobExecution execution = jobLauncher.run(batchConfiguration.job(null, null), params);

        System.out.println("Job Execution Status: " + execution.getStatus());
        System.out.println("Job executed at " + new Date());
    }

    @Scheduled(cron = "0 57 23 ? * SUN") // 매주 월요일 23시 57분 0초 실행
//    @Scheduled(cron = "0 * * * * *")
    public void runWeekJob() throws Exception {

        System.out.println("Scheduled week job is running at: " + new Date());
        String tripFavoritesHistoryRegDt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        JobParameters params = new JobParametersBuilder()
                .addString("myJob", String.valueOf(System.currentTimeMillis()))
                .addString("period", "week")
                .addString("tripFavoritesHistoryRegDt ", tripFavoritesHistoryRegDt)
                .toJobParameters();

        JobExecution execution = jobLauncher.run(batchConfiguration.job(null, null), params);

        System.out.println("Job Execution Status: " + execution.getStatus());
        System.out.println("Job executed at " + new Date());
    }

         @Scheduled(cron = "0 59 23 L * ?") // 매월 마지막날 23시 59분 0초 에 실행
//        @Scheduled(cron = "30 * * * * *")
        public void runMonthJob() throws Exception {

        System.out.println("Scheduled month job is running at: " + new Date());
        String tripFavoritesHistoryRegDt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        JobParameters params = new JobParametersBuilder()
                .addString("myJob", String.valueOf(System.currentTimeMillis()))
                .addString("period", "month")
                .addString("tripFavoritesHistoryRegDt ", tripFavoritesHistoryRegDt)
                .toJobParameters();

        JobExecution execution = jobLauncher.run(batchConfiguration.job(null, null), params);

        System.out.println("Job Execution Status: " + execution.getStatus());
        System.out.println("Job executed at " + new Date());
    }
}
