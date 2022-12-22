package day7;

import java.util.ArrayList;
import java.util.List;

public class ElfDir extends TreeNode{

    public ElfDir(){
        super("root", null, "");
        this.size = 0;
        System.out.println("Just created Dir: " + this + " with parentNode: " + this.parentNode +
                " and path: " + this.getAbsPath());

    }
    public ElfDir(String name, TreeNode parent, String pathPart){
        super(name, parent, pathPart);
        this.size = 0;
        System.out.println("Just created Dir: " + this + " with parentNode: " + this.parentNode +
                " and path: " + this.getAbsPath());

    }

    public List<TreeNode> getContents(){
        System.out.printf("Listing content of Directory -- %s --\n", this.getAbsPath());
        List<TreeNode> content = new ArrayList<>();
        for (TreeNode n: ElfDrive.nodes){
            // guard against matching parents for root node
            if (n.parentNode == null) continue;

//            if(n.parentNode.name == this.name) System.out.println(n.name + " shares parent!! + absPath: " + n.parentNode.getAbsPath());
            if (n.parentNode.getAbsPath().equalsIgnoreCase(this.getAbsPath())) {
                content.add(n);
                System.out.println(n);
            }
        }
        return content;
    }

    public int getSizeStream(){
        return this.getContents().stream()
                .map(item -> item.size)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public static int getSize(TreeNode node){
        if(node instanceof ElfFile){
            return node.size;
        } else if (node instanceof ElfDir elfDir){
            List<TreeNode> contents = elfDir.getContents();
            int size = 0;
            for (TreeNode n: contents){
                size += getSize(n);
            }
            return size;
        } else{
            throw new RuntimeException("Can't do the size thing!!!");
        }
    }

}
