package day7;

import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class TreeNode {
    public TreeNode parentNode;
    public ArrayList<TreeNode> childNodes;
    public String name;
    public int size;
    public String pathPart;

    public TreeNode(){}

    public TreeNode(String name, TreeNode parent, String pathPart){
        this.parentNode = parent;
        this.name = name;
        this.pathPart = pathPart;
        ElfDrive.nodes.add(this);
    }

    public String getAbsPath(){
        TreeNode currentNode = this;
//        System.out.println("in getAbs Path with current node: " + currentNode);

        StringBuilder sb = new StringBuilder();

        // traverse the tree upwards to root
        while(currentNode != null && currentNode.parentNode != null){
//            path = String.join("/", currentNode.pathPart, path);
            sb.insert(0,"/" + currentNode.pathPart);
            currentNode = currentNode.parentNode;
        }
        if (sb.isEmpty() )sb.append("/");
//        System.out.println("getAbsPath About to return: " + sb.toString());
        return sb.toString();
    }

    public String toString(){
        return "Node=" + name + " size=" + size;
    }

}
