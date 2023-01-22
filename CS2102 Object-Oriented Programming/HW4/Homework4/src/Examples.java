import static org.junit.Assert.*;

import org.junit.Test;

public class Examples {
	
	HeapChecker HT = new HeapChecker();
	
	// empty heap
	IHeap emptyHeap = new MtHeap();
	
	// heap with an empty right subtree
	IHeap left2 = new DataHeap(7);
	IHeap myHeap2 = new DataHeap(4, left2, emptyHeap);
	
	// heap with an empty left subtree
	IHeap right3 = new DataHeap(8);
	IHeap myHeap3 = new DataHeap(5, emptyHeap, right3);
	
	// heap with empty subtrees
	IHeap myHeap4 = new DataHeap(5);
	
	IHeap lLeft1 = new DataHeap(10);
	IHeap lRight1 = new DataHeap(12);
	IHeap rLeft1 = new DataHeap(13);
	IHeap rRight1 = new DataHeap(15);
	IHeap left1 = new DataHeap(5, lLeft1, lRight1);
	IHeap right1 = new DataHeap(7, rLeft1, rRight1);
	IHeap myHeap1 = new DataHeap(3, left1, right1);
	
	// binary trees for AddEltValidator()
	
	// binary tree after adding 5 to an empty heap
	IBinTree myBTforEmptyHeap = new DataBT(5);
	
	// binary tree after adding 3, which is smaller than the root, to an heap with empty right subtree
	IBinTree rightBTHeap2 = new DataBT(4);
	IBinTree leftBTHeap2 = new DataBT(7);
	IBinTree myBTHeap2 = new DataBT(3, leftBTHeap2, rightBTHeap2);
	
	// binary tree after adding 5, which is bigger than the root, to an heap with empty right subtree
	IBinTree rightBTHeap2_1 = new DataBT(5);
	IBinTree leftBTHeap2_1 = new DataBT(7);
	IBinTree myBTHeap2_1 = new DataBT(4, leftBTHeap2_1, rightBTHeap2_1);
	
	// binary tree after adding 3, which is smaller than the root, to an heap with empty left subtree
	IBinTree rightBTHeap3 = new DataBT(8);
	IBinTree leftBTHeap3 = new DataBT(5);
	IBinTree myBTHeap3 = new DataBT(3, leftBTHeap3, rightBTHeap3);
	
	// binary tree after adding 9, which is bigger than the root, to an heap with empty left subtree
	IBinTree rightBTHeap3_1 = new DataBT(8);
	IBinTree leftBTHeap3_1 = new DataBT(9);
	IBinTree myBTHeap3_1 = new DataBT(5, leftBTHeap3_1, rightBTHeap3_1);
	
	// binary tree after adding 7, which is bigger than the root, to a heap with empty subtree
	IBinTree lBTHeap4 = new DataBT(7);
	IBinTree myBTHeap4 = new DataBT(5, lBTHeap4, emptyHeap);
	
	// binary tree after adding 4, which is smaller than the root, to a heap with empty subtree
	IBinTree rBTHeap4_1 = new DataBT(5);
	IBinTree myBTHeap4_1 = new DataBT(4, emptyHeap, rBTHeap4_1);
	
	// SR: adding an element smaller than the root
	
	// valid heap after adding 2 to myHeap1
	IBinTree lrRightBT1 = new DataBT(12);
	IBinTree rRightBT1 = new DataBT(15);
	IBinTree rLeftBT1 = new DataBT(13);
	IBinTree lRightBT1 = new DataBT(10, new MtBT(), lrRightBT1);
	IBinTree lLeftBT1 = new DataBT(5);
	IBinTree rightBT1 = new DataBT(7, rLeftBT1, rRightBT1);
	IBinTree leftBT1 = new DataBT(3,lLeftBT1, lRightBT1);
	IBinTree myBinTree1 = new DataBT(2, leftBT1, rightBT1);
	
	// valid heap after adding 2 to myHeap1
	IBinTree rRRightBT2 = new DataBT(15);
	IBinTree rRightBT2 = new DataBT(13, new MtBT(), rRRightBT2);
	IBinTree rLeftBT2 = new DataBT(7);
	IBinTree lRightBT2 = new DataBT(12);
	IBinTree lLeftBT2 = new DataBT(10);
	IBinTree rightBT2 = new DataBT(3, rLeftBT2, rRightBT2);
	IBinTree leftBT2 = new DataBT(5, lLeftBT2, lRightBT2);
	IBinTree myBinTree2 = new DataBT(2, leftBT2, rightBT2);
	
	// binary tree, after adding 2, that has different elements from the original heap (instead of 10 and 5 we have 9 and 6) 
	IBinTree rRRightBT2_1 = new DataBT(15);
	IBinTree rRightBT2_1 = new DataBT(13, new MtBT(), rRRightBT2_1);
	IBinTree rLeftBT2_1 = new DataBT(7);
	IBinTree lRightBT2_1 = new DataBT(12);
	IBinTree lLeftBT2_1 = new DataBT(9);
	IBinTree rightBT2_1 = new DataBT(3, rLeftBT2_1, rRightBT2_1);
	IBinTree leftBT2_1 = new DataBT(6, lLeftBT2_1, lRightBT2_1);
	IBinTree myBinTree2_1 = new DataBT(2, leftBT2_1, rightBT2_1);
	
	// binary tree, after adding 2, which is not an heap
	IBinTree lrRightBT3 = new DataBT(12);
	IBinTree rRightBT3 = new DataBT(15);
	IBinTree rLeftBT3 = new DataBT(13);
	IBinTree lRightBT3 = new DataBT(10, new MtBT(), lrRightBT3);
	IBinTree lLeftBT3 = new DataBT(5);
	IBinTree rightBT3 = new DataBT(7, rLeftBT3, rRightBT3);
	IBinTree leftBT3 = new DataBT(2, lLeftBT3, lRightBT3);
	IBinTree myBinTree3 = new DataBT(3, leftBT3, rightBT3);
	
	// binary tree, after adding 2, that is missing an element (3)
	IBinTree rRightBT4 = new DataBT(15);
	IBinTree rLeftBT4 = new DataBT(13);
	IBinTree lRightBT4 = new DataBT(12);
	IBinTree lLeftBT4 = new DataBT(10);
	IBinTree rightBT4 = new DataBT(7, rLeftBT4, rRightBT4);
	IBinTree leftBT4 = new DataBT(5, lLeftBT4, lRightBT4);
	IBinTree myBinTree4 = new DataBT(2, leftBT4, rightBT4);
	
	// binary tree, after adding 2, that has an extra element (20)
	IBinTree rRRightBT5 = new DataBT(15);
	IBinTree rLLeftBT5 = new DataBT(20);
	IBinTree rRightBT5 = new DataBT(13, new MtBT(), rRRightBT5);
	IBinTree rLeftBT5 = new DataBT(7, rLLeftBT5, new MtBT());
	IBinTree lRightBT5 = new DataBT(12);
	IBinTree lLeftBT5 = new DataBT(10);
	IBinTree rightBT5 = new DataBT(3, rLeftBT5, rRightBT5);
	IBinTree leftBT5 = new DataBT(5,lLeftBT5, lRightBT5);
	IBinTree myBinTree5 = new DataBT(2, leftBT5, rightBT5);
	
	// BR: adding an element bigger than the root
	
	// valid heap after adding 6 to myHeap1
	IBinTree rRRBT1 = new DataBT(15);
	IBinTree rRBT1 = new DataBT(13, new MtBT(), rRRBT1);
	IBinTree rLBT1 = new DataBT(7);
	IBinTree lRBT1 = new DataBT(12);
	IBinTree lLBT1 = new DataBT(10);
	IBinTree rBT1 = new DataBT(6, rLBT1, rRBT1);
	IBinTree lBT1 = new DataBT(5, lLBT1, lRBT1);
	IBinTree myBT1 = new DataBT(3, lBT1, rBT1);
	
	// valid heap after adding 6 to myHeap1
	IBinTree lrRBT2 = new DataBT(12);
	IBinTree rRBT2 = new DataBT(15);
	IBinTree rLBT2 = new DataBT(13);
	IBinTree lRBT2 = new DataBT(10, new MtBT(), lrRBT2);
	IBinTree lLBT2 = new DataBT(6);
	IBinTree rBT2 = new DataBT(7, rLBT2, rRBT2);
	IBinTree lBT2 = new DataBT(5,lLBT2, lRBT2);
	IBinTree myBT2 = new DataBT(3, lBT2, rBT2);
	
	// binary tree, after adding 6, that is not heap 
	IBinTree rRRBT3 = new DataBT(15);
	IBinTree rRBT3 = new DataBT(13, new MtBT(), rRRBT1);
	IBinTree rLBT3 = new DataBT(6);
	IBinTree lRBT3 = new DataBT(12);
	IBinTree lLBT3 = new DataBT(10);
	IBinTree rBT3 = new DataBT(7, rLBT3, rRBT3);
	IBinTree lBT3 = new DataBT(5, lLBT3, lRBT3);
	IBinTree myBT3 = new DataBT(3, lBT3, rBT3);

	// binary tree, after adding 6, that is missing an element (15)
	IBinTree rRBT4 = new DataBT(13);
	IBinTree rLBT4 = new DataBT(7);
	IBinTree lRBT4 = new DataBT(12);
	IBinTree lLBT4 = new DataBT(10);
	IBinTree rBT4 = new DataBT(6, rLBT4, rRBT4);
	IBinTree lBT4 = new DataBT(5,lLBT4, lRBT4);
	IBinTree myBT4 = new DataBT(3, lBT4, rBT4);
	
	// binary tree, after adding 6, that has an extra element (20)
	IBinTree rRLBT5 = new DataBT(20);
	IBinTree rRRBT5 = new DataBT(15);
	IBinTree rRBT5 = new DataBT(13, rRLBT5, rRRBT5);
	IBinTree rLBT5 = new DataBT(7);
	IBinTree lRBT5 = new DataBT(12);
	IBinTree lLBT5 = new DataBT(10);
	IBinTree rBT5 = new DataBT(6, rLBT5, rRBT5);
	IBinTree lBT5 = new DataBT(5, lLBT5, lRBT5);
	IBinTree myBT5 = new DataBT(3, lBT5, rBT5);
	
	// binary tree, after adding 6, that has different elements from the original heap (instead of 10 and 7 we have 9 and 8)
	IBinTree rRRBT6 = new DataBT(15);
	IBinTree rRBT6 = new DataBT(13, new MtBT(), rRRBT6);
	IBinTree rLBT6 = new DataBT(8);
	IBinTree lRBT6 = new DataBT(12);
	IBinTree lLBT6 = new DataBT(9);
	IBinTree rBT6 = new DataBT(6, rLBT6, rRBT6);
	IBinTree lBT6 = new DataBT(5, lLBT6, lRBT6);
	IBinTree myBT6 = new DataBT(3, lBT6, rBT6);
	
	// hOrig, used for as a binary tree that doesn't contain the added element, or contains the minimum element respect to myHeap1
	IBinTree lLeftBT6 = new DataBT(10);
	IBinTree lRightBT6 = new DataBT(12);
	IBinTree rLeftBT6 = new DataBT(13);
	IBinTree rRightBT6 = new DataBT(15);
	IBinTree leftBT6 = new DataBT(5, lLeftBT6, lRightBT6);
	IBinTree rightBT6 = new DataBT(7, rLeftBT6, rRightBT6);
	IBinTree myBinTree6 = new DataBT(3, leftBT6, rightBT6);

	// binary trees for remMinEltValidator()
	
	// valid heap after removing the minimum element, which is 3
	IBinTree lLeftBT7 = new DataBT(12);
	IBinTree rLeftBT7 = new DataBT(13);
	IBinTree rRightBT7 = new DataBT(15);
	IBinTree leftBT7 = new DataBT(10, lLeftBT7, new MtBT());
	IBinTree rightBT7 = new DataBT(7, rLeftBT7, rRightBT7);
	IBinTree myBinTree7 = new DataBT(5, leftBT7, rightBT7);
	
	// binary tree, after removing the minimum element, that has different elements from the original heap (instead of 15 we have 18)
	IBinTree lLeftBT7_1 = new DataBT(12);
	IBinTree rLeftBT7_1 = new DataBT(13);
	IBinTree rRightBT7_1 = new DataBT(18);
	IBinTree leftBT7_1 = new DataBT(10, lLeftBT7_1, new MtBT());
	IBinTree rightBT7_1 = new DataBT(7, rLeftBT7_1, rRightBT7_1);
	IBinTree myBinTree7_1 = new DataBT(5, leftBT7_1, rightBT7_1);
	
	// valid heap after removing the minimum element, which is 3
	IBinTree lLeftBT8 = new DataBT(10);
	IBinTree rLeftBT8 = new DataBT(13);
	IBinTree rRightBT8 = new DataBT(15);
	IBinTree leftBT8 = new DataBT(7, lLeftBT8, new MtBT());
	IBinTree rightBT8 = new DataBT(12, rLeftBT8, rRightBT8);
	IBinTree myBinTree8 = new DataBT(5, leftBT8, rightBT8);
	
	// binary tree, after removing the minimum element, that is not a heap
	IBinTree lLeftBT9 = new DataBT(10);
	IBinTree rLeftBT9 = new DataBT(13);
	IBinTree rRightBT9 = new DataBT(15);
	IBinTree leftBT9 = new DataBT(5, lLeftBT9, new MtBT());
	IBinTree rightBT9 = new DataBT(12, rLeftBT9, rRightBT9);
	IBinTree myBinTree9 = new DataBT(7, leftBT9, rightBT9);
	
	// binary tree, after removing the minimum element, that is missing an element (13)
	IBinTree lLeftBT10 = new DataBT(10);
	IBinTree rRightBT10 = new DataBT(15);
	IBinTree leftBT10 = new DataBT(7, lLeftBT10, new MtBT());
	IBinTree rightBT10 = new DataBT(12, new MtBT(), rRightBT10);
	IBinTree myBinTree10 = new DataBT(5, leftBT10, rightBT10);
	
	// binary tree, after removing the minimum element, that has an extra element (20)
	IBinTree lRightBT11 = new DataBT(20);
	IBinTree lLeftBT11 = new DataBT(12);
	IBinTree rLeftBT11 = new DataBT(13);
	IBinTree rRightBT11 = new DataBT(15);
	IBinTree leftBT11 = new DataBT(10, lLeftBT11, lRightBT11);
	IBinTree rightBT11 = new DataBT(7, rLeftBT11, rRightBT11);
	IBinTree myBinTree11 = new DataBT(5, leftBT11, rightBT11);
	
	
	// tests for getRoot()
	@Test
    public void testGetRoot1(){
        assertEquals(myHeap1.getRoot(), 3, 0.1);
    }
	
	@Test
    public void testGetRoot2(){
        assertEquals(myHeap2.getRoot(), 4, 0.1);
    }
	
	@Test
    public void testGetRoot3(){
        assertEquals(myHeap3.getRoot(), 5, 0.1);
    }
	
	@Test
    public void testGetRoot4(){
        assertEquals(myHeap4.getRoot(), 5, 0.1);
    }
	
	//tests for getLeftTree() and getRightTree()
	
	@Test
    public void testGetLeftTree1(){
        assertEquals(myHeap1.getLeftTree(), left1);
    }
	
	@Test
    public void testGetRightTree1(){
        assertEquals(myHeap1.getRightTree(), right1);
    }
	
	@Test
    public void testGetLeftTree2(){
        assertEquals(myHeap2.getLeftTree(), left2);
    }
	
	@Test
    public void testGetRightTree2(){
        assertEquals(myHeap2.getRightTree(), emptyHeap);
    }
	
	@Test
    public void testGetLeftTree3(){
        assertEquals(myHeap3.getLeftTree(), emptyHeap);
    }
	
	@Test
    public void testGetRightTree3(){
        assertEquals(myHeap3.getRightTree(), right3);
    }
	
	//tests for isHeap()
	
	@Test
    public void testisHeap1(){
        assertTrue(myHeap1.isHeap());
    }
	
	@Test
    public void testisHeap2(){
        assertTrue(myHeap2.isHeap());
    }
	
	@Test
    public void testisHeap3(){
        assertTrue(myHeap3.isHeap());
    }
	
	@Test
    public void testisHeap4(){
        assertTrue(myHeap4.isHeap());
    }
	
	//tests for hasAllElements()
	
	@Test
    public void testhasAllElements1(){
        assertTrue(myBinTree1.hasAllElements(myHeap1));
    }
	
	@Test
    public void testhasAllElements2(){
        assertTrue(myBTHeap2.hasAllElements(myHeap2));
    }
	
	@Test
    public void testhasAllElements3(){
        assertTrue(myBTHeap3.hasAllElements(myHeap3));
    }
	
	@Test
    public void testhasAllElements4(){
        assertTrue(myBTHeap4.hasAllElements(myHeap4));
    }

	// tests for AddEltValidator()
	
	// tests for empty heaps or heaps with empty subtrees
	
	@Test
	// tests a valid heap after adding 5 to an empty heap
    public void testAddEltValidatorForEmptyHeap(){
        assertTrue(HT.addEltValidator(emptyHeap, 5, myBTforEmptyHeap));}
	
	@Test
	// tests a valid heap after adding 3, which is smaller than the root, to a heap with an empty right subtree
    public void testAddEltValidatorEmptyRight1(){
        assertTrue(HT.addEltValidator(myHeap2, 3, myBTHeap2));}
	
	@Test
	// tests a valid heap after adding 5, which is bigger than the root, to a heap with an empty right subtree
    public void testAddEltValidatorEmptyRight2(){
        assertTrue(HT.addEltValidator(myHeap2, 5, myBTHeap2_1));}
	
	@Test
	// tests a valid heap after adding 3, which is smaller than the root, to a heap with an empty left subtree
    public void testAddEltValidatorEmptyLeft1(){
        assertTrue(HT.addEltValidator(myHeap3, 3, myBTHeap3));}
	
	@Test
	// tests a valid heap after adding 9, which is bigger than the root, to a heap with an empty left subtree
    public void testAddEltValidatorEmptyLeft2(){
        assertTrue(HT.addEltValidator(myHeap3, 9, myBTHeap3_1));}
	
	@Test
	// tests a valid heap after adding 7, which is bigger than the root, to a heap with empty subtrees
    public void testAddEltValidatorEmptySubTrees1(){
        assertTrue(HT.addEltValidator(myHeap4, 7, myBTHeap4));}
	
	@Test
	// tests a valid heap after adding 4, which is smaller than the root, to a heap with empty subtrees
    public void testAddEltValidatorEmptySubTrees2(){
        assertTrue(HT.addEltValidator(myHeap4, 4, myBTHeap4_1));}
	
	// tests for adding an element smaller than the root
	
	@Test
	// tests a valid heap after adding 2 on the top of the heap
    public void testAddEltValidatorSR1(){
        assertTrue(HT.addEltValidator(myHeap1, 2, myBinTree1));}
	
	@Test
	// tests another valid heap after adding 2 on the top of the heap
    public void testAddEltValidatorSR2(){
        assertTrue(HT.addEltValidator(myHeap1, 2, myBinTree2));}

	@Test
	// tests for a binary tree that's not heap after adding 2
    public void testAddEltValidatorForANonHeapSR(){
        assertFalse(HT.addEltValidator(myHeap1, 2, myBinTree3));}
	
	@Test
	// tests for an heap that is missing one element, which is 3
    public void testAddEltValidatorForAnHeapMissingAnElementSR(){
        assertFalse(HT.addEltValidator(myHeap1, 2, myBinTree4));}
	
	@Test
	// tests for an heap that has an extra one element, which is 20
    public void testAddEltValidatorForAnHeapWithExtraEltSR(){
        assertFalse(HT.addEltValidator(myHeap1, 2, myBinTree5));}
	
	@Test
	// tests for an heap that has different elements from the original heap (instead of 10 and 5 we have 9 and 6)
    public void testAddEltValidatorForHavingDifferentEltsFromOriginalSR(){
        assertFalse(HT.addEltValidator(myHeap1, 2, myBinTree2_1));}
	
	@Test
	// tests for an heap that doesn't contain the added element
    public void testAddEltValidatorForMissingTheAddedEltSR(){
        assertFalse(HT.addEltValidator(myHeap1, 2, myBinTree6));}
	
	// tests for adding an element bigger than the root
	
	@Test
	// tests a valid heap after adding 6 in the heap
    public void testAddEltValidatorBR1(){
        assertTrue(HT.addEltValidator(myHeap1, 6, myBT1));}
	
	@Test
	// tests another valid heap after adding 6 in the heap
    public void testAddEltValidatorBR2(){
        assertTrue(HT.addEltValidator(myHeap1, 6, myBT2));}
	
	@Test
	// tests for a binary tree that's not heap after adding 6
    public void testAddEltValidatorForANonHeapBR(){
        assertFalse(HT.addEltValidator(myHeap1, 6, myBT3));}
	
	@Test
	// tests for an heap that is missing one element, which is 15
    public void testAddEltValidatorForAnHeapMissingAnElementBR(){
        assertFalse(HT.addEltValidator(myHeap1, 6, myBT4));}
	
	@Test
	// tests for an heap that has an extra one element, which is 20
    public void testAddEltValidatorForAnHeapWithExtraEltBR(){
        assertFalse(HT.addEltValidator(myHeap1, 6, myBT5));}
	
	@Test
	// tests for an heap that has different elements from the original heap (instead of 10 and 7 we have 9 and 8)
    public void testAddEltValidatorForHavingDifferentEltsFromOriginalBR(){
        assertFalse(HT.addEltValidator(myHeap1, 6, myBT6));}
	
	@Test
	// tests for an heap that doesn't contain the added element
    public void testAddEltValidatorForMissingTheAddedEltBR(){
        assertFalse(HT.addEltValidator(myHeap1, 6, myBinTree6));}
	
	
	// tests for remMinEltValidator()
	@Test
	// tests a valid heap after removing the minimum element
    public void testremMinEltValidator1(){
        assertTrue(HT.remMinEltValidator(myHeap1, myBinTree7));}
	
	@Test
	// tests a valid heap after removing the minimum element
    public void testremMinEltValidator2(){
        assertTrue(HT.remMinEltValidator(myHeap1, myBinTree8));}
	
	@Test
	// tests for a binary tree that's not heap after removing the minimum element
    public void testremMinEltValidatorForANonHeap(){
        assertFalse(HT.remMinEltValidator(myHeap1, myBinTree9));}
	
	@Test
	// tests for an heap that is missing one element, which is 13
    public void testremMinEltValidatorForAnHeapMissingAnElement(){
        assertFalse(HT.remMinEltValidator(myHeap1, myBinTree10));}
	
	@Test
	// tests for an heap that has an extra one element, which is 20
    public void testremMinEltValidatorForAnHeapWithExtraElt(){
        assertFalse(HT.remMinEltValidator(myHeap1, myBinTree11));}
	
	@Test
	// tests for an heap that has different elements from the original heap (instead of 15 we have 18)
    public void testremMinEltValidatorForHavingDifferentEltsFromOriginal(){
        assertFalse(HT.remMinEltValidator(myHeap1, myBinTree7_1));}
	
	@Test
	// tests for an heap that contains the removed element
    public void testremMinEltValidatorForHavingTheRemovedElt(){
        assertFalse(HT.remMinEltValidator(myHeap1, myBinTree6));}
}

