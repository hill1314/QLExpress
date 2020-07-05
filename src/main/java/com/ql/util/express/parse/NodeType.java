package com.ql.util.express.parse;

import com.ql.util.express.match.INodeType;
import com.ql.util.express.match.QLPattern;
import com.ql.util.express.match.QLPatternNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum NodeTypeKind {
    KEYWORD, //关键字 (包含 分割符号和关键字)
    BLOCK,
    EXPRESS, //表达式
    OPERATOR, //操作符
    WORDDEF,
    GROUP,
    STATEMENT
}

public class NodeType implements INodeType {
    NodeTypeManager manager;
    private String name; //名称
    private String defineStr; //内容
    private NodeTypeKind kind; //类型
    private NodeType realNodeType; //对应的真实的节点类型
    private String instructionFactory; //对应的指令工厂
    /**
     * 模式匹配
     */
    private QLPatternNode qlPatternNode;

    protected NodeType(NodeTypeManager aManager, String aName, String aDefineStr) {
        this.manager = aManager;
        this.defineStr = aDefineStr;
        this.name = aName;
    }

    public static String[][] splitProperties(String str) {
        Pattern p = Pattern.compile("(,|:)\\s*(([A-Z]|-|_)*)\\s*=");
        Matcher matcher = p.matcher(str);
        List<String[]> list = new ArrayList<String[]>();
        int endIndex = 0;
        while (matcher.find()) {
            if (list.size() > 0) {
                list.get(list.size() - 1)[1] = str.substring(endIndex, matcher.start()).trim();
            }
            list.add(new String[2]);
            list.get(list.size() - 1)[0] = str.substring(matcher.start() + 1, matcher.end() - 1).trim();
            endIndex = matcher.end();
        }
        if (list.size() > 0) {
            list.get(list.size() - 1)[1] = str.substring(endIndex).trim();
        }
        return list.toArray(new String[0][2]);
    }

    public void initial() {
        try {
            int index = this.defineStr.indexOf(":", 1);
            String[][] properties = splitProperties(this.defineStr.substring(index));
            for (String[] tempList : properties) {
                if (tempList[0].equalsIgnoreCase("type")) { //类型
                    this.kind = NodeTypeKind.valueOf(tempList[1]);
                } else if (tempList[0].equalsIgnoreCase("real")) { //真实类型
                    this.realNodeType = manager.findNodeType(tempList[1]);
                } else if (tempList[0].equalsIgnoreCase("factory")) { //指令工厂
                    this.instructionFactory = tempList[1];
                } else if (tempList[0].equalsIgnoreCase("define")) { //定义词法结构
                    this.qlPatternNode = QLPattern.createPattern(this.manager, this.name, tempList[1]);
                } else {
                    throw new RuntimeException("不能识别\"" + this.name + "\"的属性类型：" + tempList[0] + " 定义：" + this.defineStr);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("节点类型\"" + this.name + "\"初始化失败,定义：" + this.defineStr, e);
        }
    }

    public boolean isEqualsOrChild(String parent) {
        return this.manager.findNodeType(parent).isContainerChild(this);
    }

    public boolean isContainerChild(NodeType child) {
        if (this.equals(child)) {
            return true;
        }
        if (this.qlPatternNode == null) {
            return false;
        }
        if (this.qlPatternNode.isDetailMode()) {
            return ((NodeType) this.qlPatternNode.getNodeType()).isContainerChild(child);
        }
        // 是and类型，不能增加子节点或进行判断
        if (this.qlPatternNode.isAndMode() && this.qlPatternNode.getChildren().size() > 0) {
            return false;
        }
        for (QLPatternNode node : this.qlPatternNode.getChildren()) {
            if (node.getNodeType() != null && ((NodeType) node.getNodeType()).isContainerChild(child)) {
                return true;
            }
        }
        return false;
    }

    public void addChild(NodeType child) throws Exception {
        String str = child.name;
        if (this.qlPatternNode != null) {
            str = this.qlPatternNode.toString() + "|" + str;
        }
        this.qlPatternNode = QLPattern.createPattern(this.manager, this.name, str);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(name + ":TYPE=" + this.kind);
        if (this.instructionFactory != null) {
            result.append(",FACTORY=" + this.instructionFactory);
        }
        if (this.qlPatternNode != null) {
            result.append(",DEFINE=").append(this.qlPatternNode);
        }
        return result.toString();
    }

    public NodeType getRealNodeType() {
        return realNodeType;
    }

    public NodeTypeKind getKind() {
        return kind;
    }

    public String getInstructionFactory() {
        return instructionFactory;
    }

    public void setInstructionFactory(String instructionFactory) {
        this.instructionFactory = instructionFactory;
    }

    public NodeTypeManager getManager() {
        return manager;
    }

    public String getDefineStr() {
        return defineStr;
    }

    public String getName() {
        return name;
    }


    public QLPatternNode getPatternNode() {
        return this.qlPatternNode;
    }

}
