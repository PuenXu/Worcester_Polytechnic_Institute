
/**
 * the HeapChecker class helps us checking if a binary three is a valid heap after operations
 */
public class HeapChecker {
	
	/**
	 * checks if the binary tree returned, after have added an element, is a valid heap
	 * @param hOrig, the original heap
	 * @param elt, the element to add
	 * @param hAdded, the binary tree after the operation
	 * @return true if hAdded is a valid heap
	 */
	 public boolean addEltValidator(IHeap hOrig, int elt, IBinTree hAdded) {
		 //to see if it's an heap
		 return(hAdded.isHeap() && 
				 hAdded.size() == (hOrig.size() + 1) && 
				 hAdded.hasElt(elt) && !hOrig.hasElt(elt) &&
				 hOrig.allElementsExistIn(hAdded));
	 }

	 /**
		 * checks if the binary tree returned, after have removed the min element, is a valid heap
		 * @param hOrig, the original heap
		 * @param hAdded, the binary tree after the operation
		 * @return true if hAdded is a valid heap
		 */
	 public boolean remMinEltValidator(IHeap hOrig, IBinTree hRemoved) {
		 
		 if(hOrig.size() == 0) {
			 return hRemoved.size() == 0;
		 }
		 return(hRemoved.isHeap() && 
				 hRemoved.size() == (hOrig.size() - 1) && 
				 !hOrig.smallestEltExistIn(hRemoved) &&
				 hRemoved.allElementsExistIn(hOrig));
	 }
}

