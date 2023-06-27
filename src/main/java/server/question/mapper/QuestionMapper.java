package server.question.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import server.answer.dto.AnswerResponseDto;
import server.answer.entity.Answer;
import server.question.dto.QuestionDto;
import server.question.entity.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper {
    Question questionPostDtoToQuestion(QuestionDto.Post requestBody);

    Question questionPatchDtoToQuestion(QuestionDto.Patch requestBody);

    default QuestionDto.Response QuestionToQnaQuestionResponseDto(Question question, List<Answer> answers) {
        return
                QuestionDto.Response.builder()
                        .questionId(question.getQuestionId())
                        .title(question.getTitle())
                        .content(question.getContent())
                        .memberId(question.getMember().getMemberId())
                        .viewCount(question.getViewCount())
                        .answers(answerListToAnswerResponseDtoList(question.getAnswers()))
                        .createdAt(question.getCreatedAt())
                        .modifiedAt(question.getModifiedAt())
                        .build();
    }

    private List<AnswerResponseDto> answerListToAnswerResponseDtoList(List<Answer> list) {
        if (list == null) {
            return null;
        }

        List<AnswerResponseDto> list1 = new ArrayList<AnswerResponseDto>(list.size());
        for (Answer answer : list) {
            list1.add(answerToAnswerResponseDto(answer));
        }

        return list1;
    }

    private AnswerResponseDto answerToAnswerResponseDto(Answer answer) {
        if ( answer == null ) {
            return null;
        }

        AnswerResponseDto.AnswerResponseDtoBuilder answerResponseDto = AnswerResponseDto.builder();

        answerResponseDto.answerId( answer.getAnswerId() );
        answerResponseDto.questionId( answer.getQuestionId() );
        answerResponseDto.memberId( answer.getMemberId() );
        answerResponseDto.content( answer.getContent() );
        answerResponseDto.createdAt( answer.getCreatedAt() );
        answerResponseDto.modifiedAt( answer.getModifiedAt() );

        return answerResponseDto.build();
    }

    @Mapping(source = "answers", target = "answers", ignore = true)
    List<QuestionDto.Response> questionsToQuestionResponseDtos(List<Question> questions);
}