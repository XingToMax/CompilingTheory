package com.tomax.xmlutils;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lenovo on 2017/7/15.
 */
public class XmlParser {
    private List<ElementEntity> elements;

    /**
     * 初始化elements，调用递归函数getNodes，完成对节点的全遍历
     * @param document
     */
    public void setElements(Document document){
        elements = new ArrayList<ElementEntity>();
        Element root = document.getRootElement();
        getNodes(root);
    }
    /**
     *读取xml，返回document
     * @param fileName
     * @return
     * @throws MalformedURLException
     * @throws DocumentException
     */
    public static Document read(String fileName) throws MalformedURLException, DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(fileName));
        return document;
    }

    /**
     * 获取根节点
     * @param doc
     * @return
     */
    public static Element getRootElement(Document doc){
        return doc.getRootElement();
    }


    /**
     * 通过id索引节点
     * @param root
     * @param id
     * @return
     */
    public static Node findNodeById(Element root,String id){
        return root.selectSingleNode("//*[@id='"+id+"']");
    }

    /**
     * 通过坐标索引节点（仅适应此项目）
     * @param root
     * @param ltx
     * @return
     */
    public static Node findNodeByPosition(Element root,String ltx,String lty,String rbx,String rby){
        return root.selectSingleNode("//*[@ltx='"+ltx+"' and @lty='"+lty+"' and @rbx='"+rbx+"'" +
                " and @rby='"+rby+"' and @isContain='"+true+"']");
    }
    public static Node findNodeByPosition(Element root,String ltx,String lty,String rbx,String rby,int pageNum){
        return root.selectSingleNode("//page[@physical-pagenum='"+pageNum+"']/frame[@ltx='"+ltx+"' and @lty='"+lty+"' and @rbx='"+rbx+"'" +
                " and @rby='"+rby+"' and @isContain='"+true+"']");
    }


    /**
     * 批量获取节点
     * @param root
     * @param ids
     * @return
     */
    public static List<Node> findNodesByIds(Element root,List<String> ids){
        List<Node> nodes = new ArrayList<>();
        Node node = null;
        for (String in : ids){
            node = root.selectSingleNode("//*[@id='"+in+"']");
            nodes.add(node);
        }
        return nodes;
    }
    /**
     * 根据节点属性及属性对应的值获取节点
     * @param root
     * @param attr
     * @param value
     * @return
     */
    public static Node findNodeByAttrAndValue(Element root,String attr,String value){
        return root.selectSingleNode("//*[@"+attr+"='"+value+"']");
    }

    /**
     * 根据节点的属性获取节点
     * @param root
     * @param attr
     * @return
     */
    public static List<Node> findAllNodesWithAttr(Element root,String attr){
        return root.selectNodes("//*[@"+attr+"]");
    }

    /**
     * 通过节点名称索引节点
     * @param root
     * @param name
     * @return
     */
    public static Node findNodeByName(Element root,String name){
        return root.selectSingleNode("//"+name);
    }

    public static List<Node> findAllNodesByName(Element root,String name){
        return root.selectNodes("//"+name);
    }

//    public static List<Element> getElements(Element root){
//        List<Element> elements = new ArrayList<>();
//        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
//            Element element = (Element) i.next();
//            elements.add(element);
//        }
//        return elements;
//    }


    //递归初始化elementEntity
    private void getNodes(Element node){
        ElementEntity ele = new ElementEntity();
        ele.setName(node.getName());
        ele.setValue(node.getTextTrim());
        List<Attribute> listAttr=node.attributes();
        for(Attribute attr:listAttr){
            ele.getAttrNames().add(attr.getName());
            ele.getAttrValues().add(attr.getValue());
        }
        List<Element> listElement=node.elements();
        ele.setNodes(listElement);
        elements.add(ele);
        for(Element e:listElement){
            getNodes(e);
        }
    }

    /**
     * 创建一个节点，依赖的数据是ElementEntity，需要提前构造好
     * @param ele
     * @return
     */
    public static Element creatNode(ElementEntity ele){
        Element element = DocumentHelper.createElement(ele.getName());
        if (ele.getValue()!=null){
            element.setText(ele.getValue());
        }
        for (int i=0;i<ele.getAttrNames().size();i++){
            element.addAttribute(ele.getAttrNames().get(i),ele.getAttrValues().get(i));
        }
        for (int i=0;i<ele.getNodes().size();i++){
            element.add(ele.getNodes().get(i));
        }
        return element;
    }

    /**
     * 依赖id，向对应id的节点批量添加子节点
     * @param document
     * @param elements
     * @param id
     * @return
     */
    public static Document addElementToDocumentById(Document document,List<Element> elements,String id){
        Element root = getRootElement(document);
        Element target = (Element) findNodeById(root,id);
        if (target!=null){
            for (Element in : elements){
                target.add(in);
            }
        }
        return document;
    }

    /**
     * 通过标签名获取节点，并向其中添加子节点
     * @param document
     * @param elements
     * @param name
     * @return
     */
    public static Document addElementToDocumentByName(Document document,List<Element> elements,String name){
        Element root = document.getRootElement();
        Element target = (Element) findNodeByName(root,name);
        if (target!=null){
            for (Element in : elements){
                target.add(in);
            }
        }
        return document;
    }

    /**
     * 依据标签名删除节点
     * @param document
     * @param name
     * @return
     */
    public static Document deleteNodeByName(Document document,String name){
        Element root = document.getRootElement();
        Element target = (Element) findNodeByName(root,name);
        if (target!=null){
            target.getParent().remove(target);
        }
        return document;
    }


    /**
     * 通过id删除节点
     * @param document
     * @param id
     * @return
     */
    public static Document deleteNodeById(Document document,String id){
        Element root = document.getRootElement();
        Element target = (Element) findNodeById(root,id);
        if (target!=null){
            target.getParent().remove(target);
        }
        return document;
    }


    /**
     * 通过坐标删除节点(仅适用于此界面)
     * @param document
     * @param ltx
     * @return
     */
    public static Document deleteNodeByLtx(Document document,String ltx,String lty,String rbx,String rby){
        Element root = document.getRootElement();

        Element target = (Element) findNodeByPosition(root,ltx,lty,rbx,rby);
        if (target!=null){
            System.out.println(target.attributeValue("ltx")+"   "+target.attributeValue("lty")+"    "+target.attributeValue("rbx")+"    "+target.attributeValue("rby"));
            target.getParent().remove(target);
        }
        return document;
    }


    /**
     * 批量删除节点
     * @param document
     * @param ids
     * @return
     */
    public static Document deleteNodesByIds(Document document,List<String> ids){
        Element root = document.getRootElement();
        Element target = null;
        for (String in : ids){
            target = (Element)findNodeById(root,in);
            if (target!=null){
                target.getParent().remove(target);
                target = null;
            }
        }
        return document;
    }

    /**
     * 通过id更新节点内容
     * @param document
     * @param id
     * @param text
     * @return document
     */
//    public static Document updateNodeTextById(Document document,String id,String text){
//        Element root = document.getRootElement();
//        Element target = (Element) findNodeById(root,id);
//        if (target!=null){
//            target.setText(text);
//        }
//        return document;
//    }

    /**
     * 批量更新节点内容
     * @param document
     * @param ids
     * @param texts
     * @return
     */
    public static Document updateNodesTextsByIds(Document document,List<String> ids,List<String> texts){
        Element root = document.getRootElement();
        Element target =  null;
        for (int i=0;i<ids.size();i++){
            target = (Element)findNodeById(root,ids.get(i));
            if (target!=null){
                target.setText(texts.get(i));
                target = null;
            }
        }
        return document;
    }
    public static Document updateAttrByName(Document document,String tagName,String attrName,String attrValue){
        Element target = (Element) findNodeByName(document.getRootElement(),tagName);
        if (target!=null){
            target.remove(target);
            target.addAttribute(attrName,attrValue);
        }
        return document;
    }
//    public static Document addAttrByName(Document document,String tagName,String attrName,String attrValue){
//        Element target = (Element) findNodeByName(document.getRootElement(),tagName);
//        if (target!=null){
//            target.addAttribute(attrName,attrValue);
//        }
//        return document;
//    }
//    public static Document updateAttr(Document document,String attrName,String attrValue,String id){
//        Element root = document.getRootElement();
//        Element target = (Element) findNodeById(root,id);
//        if (target!=null){
//            target.attributeValue(attrName,attrValue);
//        }
//        return document;
//    }

    /**
     * 找出name标签的id最大值（id为整型）
     * @param document
     * @param name
     * @return
     */
    public static int findIdMaxValue(Document document,String name){
        List<Node> elements = findAllNodesByName(document.getRootElement(),name);
        Element element = (Element)elements.get(elements.size()-1);
        return Integer.parseInt(element.attributeValue("id"));
    }
    /**
     * 输出document，即更新xml文件
     * @param document
     * @param filePath
     * @throws IOException
     */
    public static void writeDocument(Document document,String filePath) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new FileWriter(filePath),format);
        writer.write(document);
        writer.close();
    }
    /**
     * get方法
     * @return
     */
    public List<ElementEntity> getElements() {
        return elements;
    }
    public static void main(String [] args) throws IOException, DocumentException {
        Document document = read("D:\\MySoft\\PDFTransform\\indd排\\（体例特别多）新闻英语(全)\\data.xml");
        Element element = (Element) XmlParser.findNodeByPosition(document.getRootElement(),229.6292+"",549.9851+"",308.9532+"",538.7876+"",1);
        Element element1 = element.element("segment");
        Element element2 = element1.element("span");
        System.out.println(element2.getText());
//        updateAttrByName(document,"catelogs","level","4");
//        writeDocument(document,"D:\\MySoft\\PDFTransform\\code\\PDFTransform\\res\\data.xml");

//        Element root = getRootElement(document);
//        XmlParser xmlParser = new XmlParser();
//        xmlParser.setElements(document);
//        List<ElementEntity> elementEntities = xmlParser.getElements();
//        Node element = findNodeByAttrAndValue(root,"id","1");
//        System.out.println(element.getName());
//        System.out.println(element.asXML());
//        ElementEntity elementEntity = new ElementEntity();
//        ElementEntity entity1 = new ElementEntity();
//        entity1.setName("x1");
//        entity1.setValue("2.0");
//        ElementEntity entity2 = new ElementEntity();
//        entity2.setName("y1");
//        entity2.setValue("4.0");
//        ElementEntity entity3 = new ElementEntity();
//        entity3.setName("x2");
//        entity3.setValue("6.0");
//        ElementEntity entity4 = new ElementEntity();
//        entity4.setName("y2");
//        entity4.setValue("8.0");
//        elementEntity.setName("frame");
//        elementEntity.addAttr("type","1");
//        elementEntity.addAttr("id","5");
//        elementEntity.addNode(creatNode(entity1));
//        elementEntity.addNode(creatNode(entity2));
//        elementEntity.addNode(creatNode(entity3));
//        elementEntity.addNode(creatNode(entity4));
//        Element element = creatNode(elementEntity);
//        List<Element> elements = new ArrayList<>();
//        elements.add(element);
//        document = addElementToDocumentById(document,elements,"p2");
//        writeDocument(document,"C:\\Users\\Lenovo\\Desktop\\data.xml");
    }
}
