

package trees;

import java.util.*;


class TreeNode 
{
	private String					name;
	private TreeNode				parent;
	private ArrayList<TreeNode>		children;
	
	
	TreeNode(String name)
	{
		this.name = name;
		children = new ArrayList<>();
	}
	
	
	String getName()
	{
		return name;
	}
	
	
	void addChild(TreeNode childNode)
	{
		// Add childNode to this node's children list. Also
		// set childNode's parent to this node.
		this.children.add(childNode);
		childNode.parent = this;
	}
	
	
	// Searches subtree at this node for a node
	// with the given name. Returns the node, or null if not found.
	TreeNode getNodeWithName(String targetName)
	{
		// Does this node have the target name?
		if (this.name.equals(targetName))
			return this;
				
		// No, recurse. Check all children of this node.
		for (TreeNode child: children)
		{
			// If child.getNodeWithName(targetName) returns a non-null node, 
			// then that's the node we're looking for. Return it.
			if (child.getNodeWithName(targetName) != null)
				return child.getNodeWithName(targetName);
		}
		
		// Not found anywhere.
		return null;
	}
	
	// I'm actually going to make this method return a HashSet<TreeNode>, for efficiency.
	// Returns a list of ancestors of this TreeNode, starting with this node’s parent
	// and ending with the root. Order is from recent to ancient.
	HashSet<TreeNode> collectAncestorsToList()
	{
		// I'm going to change this from an ArrayList<TreeNode> to a HashSet<TreeNode>,
		// for efficiency!
		// ArrayList<TreeNode> ancestors = new ArrayList<>();
		HashSet<TreeNode> ancestors = new HashSet<>();
		// ?????  Collect ancestors of this TreeNode into the array list. HINT: going up
		// the nodes of a tree is like traversing a linked list. If that isn’t clear,
		// draw a tree, mark any leaf node, and then mark its ancestors in order from
		// recent to ancient. Expect a question about this on the final exam.
		TreeNode nextAncestor = this.parent;
		while (nextAncestor != null)
		{
			ancestors.add(nextAncestor);
			nextAncestor = nextAncestor.parent;
		}

		return ancestors;
	}
	
	
	public String toString()
	{
		return toStringWithIndent("");
	}
	
	
	private String toStringWithIndent(String indent)
	{
		String s = indent + name + "\n";
		indent += "  ";
		for (TreeNode childNode: children)
			s += childNode.toStringWithIndent(indent);
		return s;
	}
	
	public static void main(String[] args)
	{
		// Since I changed the collectAncestorsToList() method to return a
		// HashSet<TreeNode> instead of an ArrayList<TreeNode>, I want to double-check
		// that everything still works properly.
		
		// To test collectAncestorsToList(), I should first make a TreeNode with a whole
		// bunch of ancestors, so that the collectAncestorsToList() method has something
		// to return!
		TreeNode treeNodeWithNoParents = new TreeNode("I have no parents!");
		
		// collectAncestorsToList(), when called on treeNodeWithNoParents, should return
		// an empty list!
		System.out.println(treeNodeWithNoParents.collectAncestorsToList());
		// Looks good to me! Output: []
		
		// Since it's easier to just add a bunch of descendants to treeNodeWithNoParents,
		// rather than giving it a bunch of ancestors, since the TreeNode.java class
		// has an addChild method and no addParent method, I will just add a child,
		// then add a child to that child, then add a child to that child, and so on and
		// so forth.
		TreeNode child = new TreeNode("child");
		TreeNode grandchild = new TreeNode("grandchild");
		TreeNode greatGrandchild = new TreeNode("great-grandchild");
		TreeNode greatGreatGrandchild = new TreeNode("great-great grandchild");
		
		// Make greatGreatGrandchild the child of greatGrandchild, make greatGrandchild
		// the child of grandchild, make grandchild the child of child, and make child
		// the child of treeNodeWithNoParents!
		treeNodeWithNoParents.addChild(child);
		child.addChild(grandchild);
		grandchild.addChild(greatGrandchild);
		greatGrandchild.addChild(greatGreatGrandchild);
		
		// Finally, collect the ancestors of greatGreatGrandchild! Does it work?!
		System.out.println(greatGreatGrandchild.collectAncestorsToList());
		/*
		 * It works! The output is below.
			[I have no parents!
			  child
			    grandchild
			      great-grandchild
			        great-great grandchild
			, grandchild
			  great-grandchild
			    great-great grandchild
			, child
			  grandchild
			    great-grandchild
			      great-great grandchild
			, great-grandchild
			  great-great grandchild
			]
		 */
	}
}
