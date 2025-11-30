package questions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionBank implements Serializable {
    private List<Subject> subjectList = new ArrayList<>();

    public QuestionBank() {

        List<Question> generalQuestions = new ArrayList<>();
        generalQuestions.add(new Question(
                "What is the capital city of France?",
                "Paris",
                "London",
                "Berlin",
                "Madrid"
        ));
        generalQuestions.add(new Question(
                "How many continents are there on Earth?",
                "7",
                "5",
                "6",
                "8"
        ));
        generalQuestions.add(new Question(
                "Which planet is known as the Red Planet?",
                "Mars",
                "Venus",
                "Jupiter",
                "Mercury"
        ));
        generalQuestions.add(new Question(
                "What is the largest ocean on Earth?",
                "Pacific Ocean",
                "Atlantic Ocean",
                "Indian Ocean",
                "Arctic Ocean"
        ));
        Subject s1 = new Subject("General Knowledge", generalQuestions);


        List<Question> movieQuestions = new ArrayList<>();
        movieQuestions.add(new Question(
                "Which movie features the character 'Harry Potter'?",
                "Harry Potter and the Sorcerer's Stone",
                "The Lord of the Rings",
                "Star Wars: A New Hope",
                "The Chronicles of Narnia"
        ));
        movieQuestions.add(new Question(
                "Who directed the movie 'Jurassic Park'?",
                "Steven Spielberg",
                "Christopher Nolan",
                "James Cameron",
                "Quentin Tarantino"
        ));
        movieQuestions.add(new Question(
                "In which movie does the quote 'I am your father' appear?",
                "Star Wars: The Empire Strikes Back",
                "The Matrix",
                "Titanic",
                "Avatar"
        ));
        movieQuestions.add(new Question(
                "Which movie won the Oscar for Best Picture in 1997?",
                "Titanic",
                "Braveheart",
                "Forrest Gump",
                "Gladiator"
        ));
        Subject s2 = new Subject("Movies", movieQuestions);


        List<Question> sportsQuestions = new ArrayList<>();
        sportsQuestions.add(new Question(
                "How many players are there in a football (soccer) team on the field?",
                "11",
                "9",
                "10",
                "12"
        ));
        sportsQuestions.add(new Question(
                "In which sport is the term 'love' used for a score of zero?",
                "Tennis",
                "Basketball",
                "Ice hockey",
                "Volleyball"
        ));
        sportsQuestions.add(new Question(
                "Which country won the FIFA World Cup in 2018?",
                "France",
                "Brazil",
                "Germany",
                "Argentina"
        ));
        sportsQuestions.add(new Question(
                "What is the maximum score from a single throw in darts?",
                "60",
                "50",
                "40",
                "20"
        ));
        Subject s3 = new Subject("Sports", sportsQuestions);


        List<Question> swedishHistoryQuestions = new ArrayList<>();

        swedishHistoryQuestions.add(new Question(
                "Who was the King of Sweden during the Great Northern War?",
                "Charles XII",
                "Gustav III",
                "Charles X Gustav",
                "Gustav Vasa"
        ));

        swedishHistoryQuestions.add(new Question(
                "In which year did Sweden officially adopt Christianity?",
                "Around year 1000",
                "Around year 800",
                "Around year 1200",
                "Around year 1500"
        ));

        swedishHistoryQuestions.add(new Question(
                "What was the name of the famous warship that sank in Stockholm in 1628?",
                "Vasa",
                "Kronan",
                "Mars",
                "Äpplet"
        ));

        swedishHistoryQuestions.add(new Question(
                "Which Swedish king is known for creating the modern Swedish state in the 1500s?",
                "Gustav Vasa",
                "Karl Knutsson",
                "Birger Jarl",
                "Magnus Ladulås"
        ));

        Subject s4 = new Subject("Swedish History", swedishHistoryQuestions);

        subjectList.add(s1);
        subjectList.add(s2);
        subjectList.add(s3);
        subjectList.add(s4);
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }
}

