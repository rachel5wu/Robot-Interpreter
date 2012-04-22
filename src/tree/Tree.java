package tree;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * General tree API.
 * 
 * @author David Matuszek
 * @version March 7, 2012
 * @param <T> The type of value held in each node of this Tree.
 */
public class Tree<T> {
    private T value;                      // The value held in this node
    private Tree<T> parent;               // The parent of this node
    private ArrayList<Tree<T>> children;  // The children of this node
    private int myIndex;  // The index of this node in its parent's list of children
    
// Constructors
    
    /**
     * Constructor for Tree objects. Creates a single node containing
     * the specified value.
     * 
     * @param value The value to put in the new tree node.
     */
    public Tree(T value) {
        this.value = value;
        parent = null;
        children = new ArrayList<Tree<T>>();
        myIndex = -1;
    }

// Values
    
    /**
     * Returns the value in this node of the tree.
     * 
     * @return The value in this node of the tree.
     */
    public T getValue() {
        return value;
    }
    
    /**
     * Sets the value in this node of the tree.
     * 
     * @param value The value to put in this node.
     */
    public void setValue(T value) {
        this.value = value;
    }

// Tests
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tree)) return false;
        Tree<?> that = (Tree<?>) o;
        if (!valueEquals(this, that)) return false;
        return this.value.equals(that.value)
                && this.children.equals(that.children);
    }
    
    private static boolean valueEquals(Tree<?> tree1, Tree<?> tree2) {
        if (tree1.value == null) return tree2.value == null;
        return tree1.value.equals(tree2.value);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int code = value.hashCode();
        for (Tree<T> child: children) {
            code = 41 * code + child.hashCode();
        }
        return code;
    }
    
    /**
     * Tests whether this tree node is the root.
     * 
     * @return <code>true</code> if this node has no parent.
     */
    public boolean isRoot() {
        return parent == null;
    }
    
    /**
     * Tests whether this node is a leaf.
     * 
     * @return <code>true</code> if this node has no children.
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }
    
    /**
     * Tests whether this node is a leaf.
     * 
     * @return <code>true</code> if this node has children.
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }
    
    /**
     * Tests whether this node has a sibling on its right.
     * 
     * @return <code>true</code> if this node has a right sibling.
     */
    public boolean hasNextSibling() {
        return myIndex >= 0 &&
               myIndex < parent.children.size() - 1;
    }
    
    /**
     * Tests whether this node has a sibling on its left.
     * 
     * @return <code>true</code> if this node has a left sibling.
     */
    public boolean hasPreviousSibling() {
        return myIndex > 0;
    }
    
// Getters (structure)
    
    /**
     * Returns the parent of this node, or <code>null</code> if
     * this node is the root.
     * 
     * @return the parent of this node.
     */
    public Tree<T> parent() {
        return parent;
    }

    /**
     * Returns the leftmost child of this node, or <code>null</code> if
     * this node has no children.
     * 
     * @return The leftmost child of this node.
     */
    public Tree<T> firstChild() {
        if (children.isEmpty()) return null;
        return children.get(0);
    }

    /**
     * Returns the rightmost child of this node, or <code>null</code> if
     * this node has no children.
     * 
     * @return The rightmost child of this node.
     */
    public Tree<T> lastChild() {
        if (children.isEmpty()) return null;
        return children.get(children.size() - 1);
    }
    
    /**
     * Returns a list of the children of this node.
     * 
     * @return the children of this node.
     */
    public ArrayList<Tree<T>> children() {
        return children;
    }
    
    /**
     * Returns the next child of the parent of this node,
     * or <code>null</code> if there are no additional children.
     * 
     * @return the next sibling, if any.
     */
    public Tree<T> nextSibling() {
        if (hasNextSibling()) {
            return parent.children.get(myIndex + 1);
        }
        return null;
    }
    
    /**
     * Returns the previous child of the parent of this node,
     * or <code>null</code> if there are no prior children.
     * 
     * @return the previous sibling, if any.
     */
    public Tree<T> previousSibling() {
        if (hasPreviousSibling()) {
            return parent.children.get(myIndex - 1);
        }
        return null;
    }
    
    /**
     * Returns the distance from the root of this node.
     * 
     * @return the distance from the root.
     */
    public int depth() {
        if (parent == null) return 0;
        return 1 + parent.depth();
    }
    
    /**
     * Tests whether its parameter is an ancestor node of this node.
     * A node is considered to be an ancestor of itself.
     * 
     * @param ancestor A possible ancestor of this node.
     * @return <code>true</code> if the parameter is an ancestor of this node.
     */
    public boolean hasAncestor(Tree<T> ancestor) { // including itself
        Tree<T> temp = this;
        while (temp != ancestor) {
            if (temp == null) return false;
            temp = temp.parent;
        }
        return true;
    }
    
// Setters (structure)
    
    /**
     * Adds a child to this node, following any previously existing
     * children.
     * 
     * @param newChild The node to be added as a child.
     */
    public void addChild(Tree<T> newChild) {
        if (this.hasAncestor(newChild)) {
            String message = this + " is already in " + newChild;
            throw new IllegalArgumentException(message);
        }
        int count = children.size();
        children.add(newChild);
        newChild.parent = this;
        newChild.myIndex = count;
    }
    
    /**
     * Adds any number of children to this node, following any
     * previously existing children.
     * 
     * @param newChildren The nodes to be added as children.
     */
    //@SafeVarargs
    public final void addChildren(Tree<T>... newChildren) {
        for (Tree<T> newChild : newChildren) {
            addChild(newChild);
        }
    }
    
    /**
     * Detaches this node from its parent.
     */
    public void remove() {
        if (parent == null) return;
        int decrement = 0;
        for (Iterator<Tree<T>> iter = parent.children.iterator(); iter.hasNext();) {
            Tree<T> element = iter.next();
            element.myIndex -= decrement;
            if (element == this) {
                iter.remove();
                decrement = 1;
            }
        }
    }
    
// Iterator
    
    /**
     * Returns a preorder iterator for this Tree. Each returned value
     * is a node of the tree, beginning with the root.
     * 
     * @return A preorder iterator.
     */
    public Iterator<Tree<T>> iterator() {
        return new PreorderIterator(this);
    }
    
    private class PreorderIterator implements Iterator<Tree<T>> {
        Tree<T> position;
        Tree<T> limit;
        
        PreorderIterator(Tree<T> root) {
            position = limit = root;
        }
        
        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return position != null;
        }
        
        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public Tree<T> next() {
            Tree<T> result = position;
            if (!position.isLeaf()) {
                position = position.firstChild();
            } else if (position.hasNextSibling()) {
                position = position.nextSibling();
            } else {
                do {
                    position = position.parent();
                } while (!position.hasNextSibling() && position != limit);
                if (position == limit) {
                    position = null;
                } else {
                    position = position.nextSibling();
                }
            }
            return result;
        }

        /**
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();            
        }        
    }
    
// I/O
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (isLeaf()) {
            return value.toString();
        }
        String result = value + "(";
        for (Iterator<Tree<T>> iter = children.iterator(); iter.hasNext();) {
            Tree<T> child = iter.next();
            result += child + (child.hasNextSibling() ? ", " : "");
        }
        return result + ")";
    }
    
    /**
     * Prints this Tree, in indented fashion.
     */
    public void print() {
        print(this, "");
    }   
    
    private void print(Tree<T> node, String indent) {
        if (node == null) return;
        System.out.println(indent + node.value);
        for (Iterator<Tree<T>> iter = node.children.iterator(); iter.hasNext();) {
            print(iter.next(), indent + "   ");
        }
    }
}
