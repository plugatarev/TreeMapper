package com.github.plugatarev.mapper;

import com.github.plugatarev.dto.TreeDTO;
import com.github.plugatarev.entity.TreeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TreeMapperTest {

    private final static SortById SORTER = new SortById();

    @Test
    public void oneNodeInTreeCorrectTest() {
        List<TreeEntity> treeEntities = List.of(new TreeEntity(1, "1", null));
        Collection<TreeDTO> expected = List.of(new TreeDTO(1, "1", null));

        Collection<TreeDTO> result = TreeMapper.convert(treeEntities);

        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void oneParentWithThreeChildrenCorrectTest() {
        List<TreeEntity> treeEntities = new ArrayList<>();
        treeEntities.add(new TreeEntity(1, "1", null));
        treeEntities.add(new TreeEntity(2, "2", 1));
        treeEntities.add(new TreeEntity(3, "3", 1));
        treeEntities.add(new TreeEntity(4, "4", 1));

        Collection<TreeDTO> expected = new ArrayList<>();
        List<TreeDTO> children = new ArrayList<>();
        children.add(new TreeDTO(2, "2"));
        children.add(new TreeDTO(3, "3"));
        children.add(new TreeDTO(4, "4"));
        expected.add(new TreeDTO(1, "1", children));
        expected.add(new TreeDTO(2, "2"));
        expected.add(new TreeDTO(3, "3"));
        expected.add(new TreeDTO(4, "4"));
        expected = expected.stream().sorted(SORTER).toList();


        Collection<TreeDTO> result = TreeMapper.convert(treeEntities);
        result = result.stream().sorted(SORTER).toList();

        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void treeIsLinkedListCorrectTest() {
        final int nodeCount = 20;
        List<TreeEntity> treeEntities = new ArrayList<>();
        treeEntities.add(new TreeEntity(0, "0", null));

        for (int i = 1; i < nodeCount; i++) {
            treeEntities.add(new TreeEntity(i, String.valueOf(i), i - 1));
        }

        Collection<TreeDTO> expected = new ArrayList<>();
        TreeDTO prev = null;
        for (int i = nodeCount - 1; i >= 0; i--) {
            prev = (i == nodeCount - 1) ? new TreeDTO(i, String.valueOf(i), null) :
                                          new TreeDTO(i, String.valueOf(i), List.of(prev));
            expected.add(prev);
        }
        expected = expected.stream().sorted(SORTER).toList();

        Collection<TreeDTO> result = TreeMapper.convert(treeEntities);
        result = result.stream().sorted(SORTER).toList();

        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void emptyInputTreeEntitiesCorrectTest() {
        List<TreeEntity> treeEntities = new ArrayList<>();

        Collection<TreeDTO> result = TreeMapper.convert(treeEntities);

        Assertions.assertEquals(new ArrayList<>().toString(), result.toString());
    }

    @Test
    public void difficultTreeCorrectTest() {
        List<TreeEntity> treeEntities = new ArrayList<>();
        treeEntities.add(new TreeEntity(1, "1", 7));
        treeEntities.add(new TreeEntity(2, "2", 1));
        treeEntities.add(new TreeEntity(3, "3", 1));
        treeEntities.add(new TreeEntity(4, "4", 1));
        treeEntities.add(new TreeEntity(5, "5", 1));
        treeEntities.add(new TreeEntity(6, "6", 7));
        treeEntities.add(new TreeEntity(7, "7", null));
        treeEntities.add(new TreeEntity(8, "8", 7));

        List<TreeDTO> expected = new ArrayList<>();
        TreeDTO dto2 = new TreeDTO(2, "2", null);
        TreeDTO dto3 = new TreeDTO(3, "3", null);
        TreeDTO dto4 = new TreeDTO(4, "4", null);
        TreeDTO dto5 = new TreeDTO(5, "5", null);
        TreeDTO dto6 = new TreeDTO(6, "6", null);
        TreeDTO dto8 = new TreeDTO(8, "8", null);
        TreeDTO dto1 = new TreeDTO(1, "1", List.of(dto2, dto3, dto4, dto5));
        TreeDTO dto7 = new TreeDTO(7, "7", List.of(dto1, dto6, dto8));

        expected.add(dto1);
        expected.add(dto2);
        expected.add(dto3);
        expected.add(dto4);
        expected.add(dto5);
        expected.add(dto6);
        expected.add(dto7);
        expected.add(dto8);
        expected = expected.stream().sorted(SORTER).toList();

        Collection<TreeDTO> result = TreeMapper.convert(treeEntities);
        result = result.stream().sorted(SORTER).toList();

        Assertions.assertEquals(expected.toString(), result.toString());

    }

    @Test
    public void convertArgumentIsNullException() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> TreeMapper.convert(null)
        );

        Assertions.assertEquals("Parameter 'entities' cannot be null", thrown.getMessage());
    }

    static class SortById implements Comparator<TreeDTO> {
        public int compare(TreeDTO a, TreeDTO b) {
            return a.getId() - b.getId();
        }
    }
}
