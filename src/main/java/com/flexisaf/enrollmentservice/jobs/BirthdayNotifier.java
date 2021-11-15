package com.flexisaf.enrollmentservice.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class BirthdayNotifier extends QuartzJobBean {

    public static JobDetail buildJobDetail(Long userId){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("userId", userId);

        return JobBuilder.newJob(BirthdayNotifier.class)
                .withIdentity(UUID.randomUUID().toString())
                .withDescription("Send BirthDay Notification")
                .usingJobData(jobDataMap)
                .storeDurably().build();
    }

    public static Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Send Birthday Notification Email Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Executing Job with key {}", context.getJobDetail().getKey());

        JobDataMap jobDataMap = context.getMergedJobDataMap();

        Long userId = jobDataMap.getLong("userId");


        //send email to userId using email sender
       log.info("Sending BirthDay Notification to User with ID "+userId);
    }
}
