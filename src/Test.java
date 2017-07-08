import com.sun.deploy.util.ArrayUtil;
import com.sun.org.apache.xml.internal.utils.StringBufferPool;

import java.util.*;
import java.util.stream.Collectors;

public class Test {
private boolean a = true;
    public static void main(String[] args)   // unit testing (optional)
    {

        System.out.println(merge(new String[]{"abc","def","def","abc"}));
    }
public static String merge(String[] votes){
    List<Candidate> candidates = new ArrayList<>();
    int counter = 0;
    for(String vote : votes) {
       boolean cont = false;
        Candidate candidate = null;
        for(Candidate c : candidates){
            if(c.getName().equals(vote)) {
                cont = true;
                candidate = c;
                break;
            }
        }
            if(cont)
        {
            candidate.setVotes(candidate.getVotes()+1);
            candidate.setPosition(counter);

            }
            else{
                candidates.add(new Candidate(vote,1,counter));
            }

counter++;
    }
    Comparator<Candidate> byVote = (c1, c2) -> Integer.compare(
            c1.getVotes(), c2.getVotes());
    Comparator<Candidate> byPosition = (c1, c2) -> Integer.compare(
            c1.getPosition(), c2.getPosition());
     candidates = candidates.stream().sorted(byVote.thenComparing(byPosition)).collect(Collectors.toList());
    return candidates.get(candidates.size()-1).getName();
}


}
 class Candidate {
        private String name;
    private int votes;
    private int position;


    public Candidate(String name, int votes, int position) {
        this.name = name;
        this.votes = votes;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }




}
