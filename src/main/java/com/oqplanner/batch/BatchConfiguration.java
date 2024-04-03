package com.oqplanner.batch;


import com.oqplanner.tripfavorites.domain.TripFavoritesHistory;
import com.oqplanner.tripranking.domain.TripRanking;
import com.oqplanner.tripranking.mapper.TripRankingMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

@Configuration
@Component
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfiguration {


    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Autowired
    TripRankingMapper tripRankingMapper;

    @Autowired PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job job(JobRepository jobRepository, Step dayStep) throws Exception{
        return new JobBuilder("myJob", jobRepository)
                .start(dayStep(jobRepository, null, null))
                .build();
    }


    @Bean
    @JobScope
    public Step dayStep(JobRepository jobRepository, @Value("#{jobParameters[period]}") String period,
                        @Value("#{jobParameters[tripFavoritesHistoryRegDt]}") String tripFavoritesHistoryRegDt) throws Exception {
        System.out.println("period ~~~ " + period);
        System.out.println("tripFavoritesHistoryRegDt ~~~ " + tripFavoritesHistoryRegDt);
        return new StepBuilder("dayStep",jobRepository)
                .<TripRanking, TripRanking>chunk(10, platformTransactionManager)
                .reader(reader(period, tripFavoritesHistoryRegDt))
//                .processor(processor())
                .writer(writer(period))
                .build();
    }

    @Bean
    @StepScope
    public MyBatisPagingItemReader<TripRanking> reader(
            @Value("#{jobParameters[period]}") String period,
            @Value("#{jobParameters[tripFavoritesHistoryRegDt]}") String tripFavoritesHistoryRegDt) throws Exception {
        /*
         * Paging 처리 시 OrderBy는 필수
         */

//        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        System.out.println("currentDate ::: " + currentDate);
        System.out.println("period ::: " + period);
        System.out.println("tripFavoritesHistoryRegDt ::: " + tripFavoritesHistoryRegDt);
        MyBatisPagingItemReader<TripRanking> reader = new MyBatisPagingItemReader<>();
        reader.setPageSize(10);
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setQueryId("getTripFavoritesHistory");
        reader.setParameterValues(Collections.singletonMap("tripFavoritesHistoryRegDt",tripFavoritesHistoryRegDt));
        reader.setParameterValues(Collections.singletonMap("period", period));
        return reader;
    }



    @Bean
    public ItemProcessor<TripRanking, TripRanking> processor() throws Exception {
        return new ItemProcessor<TripRanking, TripRanking>() {
            @Override
            public TripRanking process(TripRanking model) throws Exception {

                TripRanking ranking = new TripRanking();
                ranking.setTripRankingOrder(model.getTripRankingOrder());
                ranking.setTripRankingFavoritesNum(model.getTripRankingFavoritesNum());
                ranking.setTripFavoritesOutId(model.getTripFavoritesOutId());
                ranking.setTripFavoritesImgUrl(model.getTripFavoritesImgUrl());
                ranking.setTripFavoritesNm(model.getTripFavoritesNm());
                return ranking;
            }
        };
    }

    @Bean
    @StepScope
    public MyBatisBatchItemWriter<TripRanking> writer(@Value("#{jobParameters[period]}") String period) throws Exception{
        System.out.println("call writer");
        MyBatisBatchItemWriter<TripRanking> writer = new MyBatisBatchItemWriter<>();
        writer.setSqlSessionFactory(sqlSessionFactory);


        if(period.equals("day")) {
            writer.setStatementId("com.oqplanner.tripranking.mapper.TripRankingMapper.saveTripDayRanking");
        }else if(period.equals("week")) {
            writer.setStatementId("com.oqplanner.tripranking.mapper.TripRankingMapper.saveTripWeekRanking");
        }else if(period.equals("month")) {
            writer.setStatementId("com.oqplanner.tripranking.mapper.TripRankingMapper.saveTripMonthRanking");
        }
        return writer;
    }

}
