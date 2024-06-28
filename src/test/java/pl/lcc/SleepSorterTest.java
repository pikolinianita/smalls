package pl.lcc;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import static org.assertj.core.api.Assertions.*;

class SleepSorterTest {

    @Test
    void testIntList(){
        var source = new ArrayList<>(List.of(1,2,3,4,5,6,7,11,24));
        Collections.shuffle(source);
        var sut = new SleepSorter<Integer>();
        var result = sut.sort(source, Function.identity());
        assertThat(result).containsSequence(1,2,3,4,5,6,7,11,24);
    }

    @Test
    void testObjList(){
        List<Plane> source = List.of(
                new Plane ("Spitfire", 80, 1),
                new Plane ("B17", 78, 8),
                new Plane ("Gulfstream", 10, 20),
                new Plane ("Cessna", 20, 4)
        );
        var sut = new SleepSorter<Plane>();
        var sa = new SoftAssertions();
        sa.assertThat(sortedNames(sut, source, Plane::age)).containsSequence("Gulfstream", "Cessna", "B17", "Spitfire");
        sa.assertThat(sortedNames(sut, source, Plane::capacity)).containsSequence("Spitfire", "Cessna", "B17", "Gulfstream");
        sa.assertThat(sortedNames(sut, source, p -> p.name().length())).containsSequence("B17","Cessna", "Spitfire", "Gulfstream");
        sa.assertAll();
    }

    private List<String> sortedNames(SleepSorter<Plane> sut, List<Plane> source, Function <Plane,Integer> f) {
        return sut.sort(source,f).stream().map(Plane::name).toList();
    }

    @Test
    void testLongList(){
        var source = new ArrayList<>(List.of(1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2));
        var sut = new SleepSorter<Integer>();
        var result = sut.sort(source, Function.identity());
        assertThat(result).doesNotContainSequence(2,1);
    }

}

record Plane(String name, Integer age, Integer capacity){}