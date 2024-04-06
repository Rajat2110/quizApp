package com.rajat.quizApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajat.quizApp.model.Question;
import com.rajat.quizApp.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    private Question question;
    private Question inputQuestion;

    @BeforeEach
    public void setUp() {
        question = Question.builder()
                .questionTitle("Number of primitive data types in Java are?")
                .option1("6")
                .option2("7")
                .option3("8")
                .option4("9")
                .rightAnswer("8")
                .difficultyLevel("easy")
                .category("java")
                .build();

        inputQuestion = Question.builder()
                .questionTitle("Number of primitive data types in Java are?")
                .option1("6")
                .option2("7")
                .option3("8")
                .option4("9")
                .rightAnswer("8")
                .difficultyLevel("easy")
                .category("java")
                .build();
    }

    @Test
    public void testGetAllQuestions() throws Exception {
        List<Question> questionList = new ArrayList<>();
        questionList.add(question);

        ResponseEntity<List<Question>> responseEntity = ResponseEntity.ok(questionList);
        Mockito.when(questionService.getAllQuestions())
                .thenReturn(responseEntity);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/question/allQuestions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the JSON content from the MvcResult
        String jsonResponse = mvcResult.getResponse().getContentAsString();

        // Convert the JSON response directly into an array of Question objects
        ObjectMapper objectMapper = new ObjectMapper();
        Question[] actualQuestions = objectMapper.readValue(jsonResponse, Question[].class);

        // Convert the array of questions into a List for comparison
        List<Question> retrievedQuestionList = Arrays.asList(actualQuestions);

        // Assert that the actual questions list matches the expected list
        assertEquals(questionList, retrievedQuestionList);
    }

    @Test
    public void testGetQuestionByCategory() throws Exception {

        String category = "Java";
        List<Question> questionList = new ArrayList<>();
        questionList.add(question);
        ResponseEntity<List<Question>> quesByCategory = new ResponseEntity<>(questionList, HttpStatus.OK);

        Mockito.when(questionService.getQuestionsByCategory(category))
                .thenReturn(quesByCategory);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/question/category/{cat}", category))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Question[] actualQuestions = objectMapper.readValue(jsonResponse, Question[].class);

        List<Question> retrievedQuestionList = Arrays.asList(actualQuestions);

        assertEquals(questionList, retrievedQuestionList);
    }

    @Test
    public void testAddQuestion() throws Exception {

        ResponseEntity<String> message = new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
        Mockito.when(questionService.addQuestion(inputQuestion))
                .thenReturn(message);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/question/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\t\"questionTitle\":\"Number of primitive data types in Java are?\",\n" +
                                "\t\"option1\":\"6\",\n" +
                                "\t\"option2\":\"7\",\n" +
                                "\t\"option3\":\"8\",\n" +
                                "\t\"option4\":\"9\",\n" +
                                "\t\"rightAnswer\":\"8\",\n" +
                                "\t\"difficultyLevel\":\"easy\",\n" +
                                "\t\"category\":\"java\"\n" +
                                "}"))
                .andExpect(status().isCreated());

    }

    @Test
    public void testUpdateQuestion() throws Exception {

        Integer questionId = 1;
        ResponseEntity<String> message = new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
        Mockito.when(questionService.updateQuestion(questionId, inputQuestion))
                .thenReturn(message);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/question/update/{id}", questionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\t\"questionTitle\":\"Number of primitive data types in Java are?\",\n" +
                                "\t\"option1\":\"6\",\n" +
                                "\t\"option2\":\"4\",\n" +
                                "\t\"option3\":\"8\",\n" +
                                "\t\"option4\":\"12\",\n" +
                                "\t\"rightAnswer\":\"8\",\n" +
                                "\t\"difficultyLevel\":\"easy\",\n" +
                                "\t\"category\":\"java\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteQuestion() throws Exception {

        Integer id = 1;

        ResponseEntity<String> message = new ResponseEntity<>(Mockito.anyString(), HttpStatus.OK);
        Mockito.when(questionService.deleteQuestion(id))
                .thenReturn(message);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/question/delete/{id}", id))
                .andExpect(status().isOk());
    }
}
