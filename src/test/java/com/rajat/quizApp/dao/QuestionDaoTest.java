package com.rajat.quizApp.dao;

import com.rajat.quizApp.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class QuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @BeforeEach
    void setUp() {
        Question question1 = new Question(1,"What is the extension of compiled java classes?",
                ".js", ".txt", ".class", ".java", ".class", "easy", "java");

        Question question2 = new Question(2, "Which of the following is used to define a block of code in Python language?",
                "Indentation", "Key", "Brackets", "All of the mentioned", "Indentation", "easy", "python");

        List<Question> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);
        questionDao.saveAll(questions);
    }

    @Test
    public void testFindByCategory() {
        List<Question> quesByCategory = questionDao.findByCategory("python");
        assertEquals(1, quesByCategory.size());
    }

    @Test
    public void testFindAll() {
        List<Question> allQuestions = questionDao.findAll();
        assertNotNull(allQuestions);
        assertEquals(2, allQuestions.size());
    }

    @Test
    public void testDelete() {
        questionDao.deleteById(2);
        List<Question> remainingQuestions = questionDao.findAll();
        assertEquals(1, remainingQuestions.size());
    }

}
