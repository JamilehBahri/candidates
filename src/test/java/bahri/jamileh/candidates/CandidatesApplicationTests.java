package bahri.jamileh.candidates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CandidatesApplicationTests {

    private Set<String> s1;
    private Set<String> s2;

    private Set<String> sameItem;
    private Set<String> diffrentItem;

    @Before
    public void init(){

        s1 = new HashSet<>();
        s1.add("1");
        s1.add("2");
        s1.add("3");
        s1.add("4");
        s1.add("5");

        s2 = new HashSet<>();
        s2.add("1");
        s2.add("2");
        s2.add("3");
        s2.add("6");
        s2.add("7");

    }

    @Test
    public void diff() {

   //     System.out.println("s1.retainAll(s2) is :"+s1.retainAll(s2));

        for(String s : s1){
            for (String ss:s2)
                if(s.contains(ss))
                    sameItem.add(s);
                else
                    diffrentItem.add(s);
        }
        System.out.println("same Item" + sameItem);
        System.out.println("diffrent Items: " + diffrentItem);

    }



}
