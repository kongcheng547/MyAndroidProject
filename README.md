## <center>小学期实践 </center>

###### #作业2第二题，统计一个页面中的某控件的数目，可以用以下代码实现（网上查阅获得）

 ``java ``

 ``private static int labelNum(String filePath, String labelName) ``

 ``{ ``

 	``int count = 0; ``

 	``try { ``

 		``	File f = new File(filePath); ``

 		``DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); ``

​	 	``DocumentBuilder builder = factory.newDocumentBuilder();``

 		`` Document doc = builder.parse(f); `` 

 		``NodeList nl = doc.getElementsByTagName(labelName); ``

 		``for (int i = 0; i < nl.getLength(); i++) ``

​	 	``{ ``

 			``count++; ``

 		``} ``

 	``} ``

 	``catch (Exception e)``

 	`` { `` 

​		 ``e.printStackTrace(); `` 

 	``} `` 

 ``return count; ``

 ``} ``

###### #作业三为Homework文件夹，暂时还未做好第三个作业

###### #7.11完成了作业三的选做内容

###### #作业四是project4文件夹

###### 作业五是project5文件夹，关于post和get的区别在于里面的README.md文件

###### 作业六是project6文件夹