package com.rajat.quizApp.service;

import com.rajat.quizApp.dao.QuestionDao;
import com.rajat.quizApp.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @MockBean
    private QuestionDao questionDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllQuestions() {
        Question question = new Question(1,"What is the extension of compiled java classes?",
                ".js", ".txt", ".class", ".java", ".class", "easy", "java");

        List<Question> questionList = new ArrayList<>();
        questionList.add(question);

        //mock repository behavior
        Mockito.when(questionDao.findAll()).thenReturn(questionList);

        //call service method
        ResponseEntity<List<Question>> responseEntity = questionService.getAllQuestions();

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(questionList, responseEntity.getBody());
    }

    @Test
    public void testGetQuestionByCategory() {
        Question question = new Question(1,"What is the extension of compiled java classes?",
                ".js", ".txt", ".class", ".java", ".class", "easy", "java");
        String category = "java";
        List<Question> questionList = new ArrayList<>();
        questionList.add(question);

        //mock repository behavior
        Mockito.when(questionDao.findByCategory(category)).thenReturn(questionList);

        //call service method
        ResponseEntity<List<Question>> responseEntity = questionService.getQuestionsByCategory(category);

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(questionList, responseEntity.getBody());
    }

    @Test
    public void testAddQuestion() {
        Question question = new Question(1,"What is the extension of compiled java classes?",
                ".js", ".txt", ".class", ".java", ".class", "easy", "java");

        Mockito.when(questionDao.save(question)).thenReturn(question);

        ResponseEntity<String> responseEntity = questionService.addQuestion(question);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Question added successfully", responseEntity.getBody());
    }

    @Test
    public void testUpdateQuestion() {
        Question existingQuestion = new Question(1,"What is the extension of compiled java classes?",
                ".js", ".txt", ".class", ".java", ".class", "easy", "java");

        Question updatedQuestion = new Question(1,"What is the extension of compiled java classes?",
                ".js", ".bin", ".class", ".java", ".class", "easy", "java");

        Integer id = 1;

        Mockito.when(questionDao.findById(id)).thenReturn(Optional.of(existingQuestion));
        Mockito.when(questionDao.save(existingQuestion)).thenReturn(updatedQuestion);

        ResponseEntity<String> responseEntity = questionService.updateQuestion(id, updatedQuestion);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Updated Successfully", responseEntity.getBody());
    }

    @Test
    public void testDeleteQuestion() {
        Question question = new Question(1,"What is the extension of compiled java classes?",
                ".js", ".txt", ".class", ".java", ".class", "easy", "java");
        Integer id = 1;
        Mockito.when(questionDao.findById(id)).thenReturn(Optional.of(question));

        ResponseEntity<String> responseEntity = questionService.deleteQuestion(id);

        Mockito.verify(questionDao).deleteById(id); //verify deleteById is called

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Deleted Successfully", responseEntity.getBody());
    }
}
