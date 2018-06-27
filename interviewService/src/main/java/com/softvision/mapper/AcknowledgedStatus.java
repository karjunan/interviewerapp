package com.softvision.mapper;

import com.softvision.constant.InterviewConstant;
import com.softvision.exception.ServiceException;
import com.softvision.model.Interview;
import com.softvision.model.InterviewStatus;
import com.softvision.model.Interviewlog;
import com.softvision.service.InterviewService;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class AcknowledgedStatus {

    @Inject
    InterviewService interviewService;


    public Optional<Interviewlog> acknowledgedInterview(String candidateId, String interviewId) throws ServiceException {
        Long count = interviewService.getCandidateCount(candidateId);
        if (count != 0) {
            throw  new ServiceException(InterviewConstant.CANDIDATE_ACK);
        } else {

            Interview interview = (Interview)interviewService.getInterviewByCandidateId(
                    candidateId,InterviewStatus.INITIATED.toString()).get();
            LocalDateTime joiningDate = LocalDateTime.now();

            // move int to previous log
            Interviewlog intLogInterview = new Interviewlog();
            intLogInterview.setInterviewStatus(interview.getInterviewStatus());
            intLogInterview.setCandidateId(interview.getCandidateId());
            intLogInterview.setInterviewerId(interview.getInterviewerId());
            intLogInterview.setModifiedDate(interview.getModifiedDate());
            intLogInterview.setCreationTime(interview.getCreationTime());
            intLogInterview.setInterviewId(interview.getId());
            intLogInterview.setInterviewerList(interview.getInterviewerList());
            interviewService.addInterviewLog(intLogInterview);

            interviewService.deleteInterview(interview.getId());

            Interview ackInterview = new Interview();
            ackInterview.setInterviewStatus(InterviewStatus.ACKNOWLEDGED);
            ackInterview.setCandidateId(candidateId);
            ackInterview.setInterviewerId(interviewId);
            ackInterview.setModifiedDate(joiningDate);
            ackInterview.setCreationTime(joiningDate);
            ackInterview.setInterviewerList(null);
            return  interviewService.addInterview(ackInterview);
        }
    }

}



