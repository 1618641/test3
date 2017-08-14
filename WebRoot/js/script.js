function add(t,loc){
	
	//新增选项卡之前，判断tabs中，是否已经存在指定的标题
	var flag = $("#mydiv").tabs("exists",t);
	
	if(flag){
		//标题已存在-------选中已存在的这个标题
		$("#mydiv").tabs("select",t);
	}else{
		//不存在
		$("#mydiv").tabs('add',{
			title:t,
			content:"<iframe name='right' src='"+loc+".jsp' frameborder='0' style='width:100%;height:100%'></iframe>",
			iconCls:"icon-add",
			closable:true
		});
	}
}


var op="add";

$(function(){
	
	//查询
	$("#searchBtn").click(function(){
		var name = $("[name=name]").val();
		var clazz = $("[name=clazz]").val();
		var gender = $("[name=gender]").val();
		var result = $("[name=result]").val();
		
		//如果调用datagrid的load方法，系统就会重新请求datagrid中url指定的地址
		$("#mytab").datagrid("load",{"name":name,"clazz":clazz,"gender":gender,"result":result});
	});

	 $("#saveBtn").click(function(){
		 if(op=="add"){
			 //添加
			 //首先要判断，用户输入的数据，是否已经完整
			 var flag = $("#frm").form("validate");//返回 boolean,true表示验证通过
			 if(flag){//验证通过，才进行添加操作
	    		 //获得表单，把表单序列化以后，向后台传递
	    		 $.ajax({
	    			 url:"ajax?method=add",
	    			 type:"post",
	    			 data:$("#frm").serialize(),
	    			 success:function(){
	    				//右下角提示
	    				 $.messager.show({
	    					 msg:"添加成功",
	    					 title:"提示"
	    				 });
	    				//重置 表单数据
	    				 $("#frm")[0].reset();
	    				 //关闭窗口
	    				 $("#mydiv").dialog("close");
	    				//让datagrid显示最新的数据
	    				 $("#mytab").datagrid("reload");
	    			
	    			 }
	    		 });
			 }
		 }else{
			 //修改
			 //首先要判断，用户输入的数据，是否已经完整
			 var flag = $("#frm").form("validate");//返回 boolean,true表示验证通过
			 if(flag){//验证通过，才进行添加操作
	    		 //获得表单，把表单序列化以后，向后台传递
	    		 $.ajax({
	    			 url:"ajax?method=update",
	    			 type:"post",
	    			 data:$("#frm").serialize(),
	    			 success:function(){
	    				//右下角提示
	    				 $.messager.show({
	    					 msg:"修改成功",
	    					 title:"提示"
	    				 });
	    				//重置 表单数据
	    				 $("#frm")[0].reset();
	    				 //关闭窗口
	    				 $("#mydiv").dialog("close");
	    				//让datagrid显示最新的数据
	    				 $("#mytab").datagrid("reload");
	    			
	    			 }
	    		 });
			 }
		 }
	 });

	$("#mytab").datagrid({
		toolbar:[
		         
		         {"text":"添加","iconCls":"icon-add","handler":function(){
		        	//先编辑对话框的内容，再打开窗口
		        	 $("#mydiv").dialog({
		        		 title:"添加",
		        		 iconCls:"icon-add"
		        	 });
		        	 $("#mydiv").dialog("open");//点击的时候，打开面板
		        	 
		        	//让标识符的值，变为add,表示要做添加
	    			op="add";
		         }},
		         {"text":"删除","iconCls":"icon-remove",handler:function(){
		        		 //删除
			        	 //获得datagrid中选中的行------------返回行的数组
			        	 var rows = $("#mytab").datagrid("getSelections");//所有选中的行
			        	 if(rows.length==0){
			        		 $.messager.show({
			        			 title:"提示",
			        			 msg:"请选择要删除的行"
			        			 
			        		 });
			        	 }else{
			        		 $.messager.confirm("删除","你确定要删除该数据吗?",function(b){
			        			 if(b){
			        				 var ids="";//用于保存所有要删除的id
					        		 //选中的有行，可能是一行，也可能是多行
					        		 for(var i=0;i<rows.length;i++){
					        			 var r = rows[i];//分别获得每一行
					        			 ids+=r.id+","
					        		 }
					        		$.post("ajax",{"ids":ids,"method":"del"},function(){
					        			//右下角提示
					    				 $.messager.show({
					    					 msg:"删除成功",
					    					 title:"提示"
					    				 });
					    					//让datagrid显示最新的数据
					    				 $("#mytab").datagrid("reload");
					        		});
			        			 }
			        		 });
			        	 }  	
		         }},
		         {"text":"修改","iconCls":"icon-edit","handler":function(){

		        	 /********************************************************************/
		        	 //得到用户选中要修改的行
		        	 var rows = $("#mytab").datagrid("getSelections");//所有选中的行---数组
		        	
		        	 //判断选中的行是为0,或者大于1，给出提示信息//如果只选中了一行，就获得这一行，然后把这一行的每一列的值，分别显示在控件中
		        	 if(rows.length==0){
		        		 $.messager.show({
		        			 title:"提示",
		        			 msg:"请选择要修改的行"
		        			 
		        		 });
		        	 }else if(rows.length>1){//选中了多行
		        		 $.messager.show({
		        			 title:"提示",
		        			 msg:"只能选择一行数据进行修改"
		        			 
		        		 });
		        		 
		        	 }else{
					      //先编辑对话框的内容，再打开窗口
			        	 $("#mydiv").dialog({
			        		 title:"修改",
			        		 iconCls:"icon-edit"
			        	 });
			        	 $("#mydiv").dialog("open");//点击的时候，打开面板
			        	 //标识要做修改操作
			        	 op="update";
			        	 /**************************************/
			        	 //获得选中的这一行
			        	 var r = rows[0];
			        	 //把这一行每一列的值，分别显示在控件中
			        	 $("#frm").form("load",{"id":r.id,"name":r.name,"gender":r.gender,"clazz":r.clazz,"score":r.score,"bir":r.bir});
		        	 } 
		         }},
		         {"text":"查询","iconCls":"icon-search",handler:function(){
		        	 $("#searchDiv").panel("expand");//展开
		         }},
		         {"text":"查看图表","iconCls":"icon-save",handler:function(){
		        	 window.open("charts.jsp","","width=1150px,height=550px,location=no,top=100px,left=100px,resizable=no");
		         }},
		         {"text":"导出数据","iconCls":"icon-save",handler:function(){
		        	//$.post("ajax?method=export");
		        	 window.location.href="ajax?method=export";
		         }}
		         
		],
		url:"ajax?method=load",//请求地址
		method:"post",//设置请求方式，默认是post
		striped:true,//表格条纹化
		//fit:true,//填充父窗口
		loadMsg:"数据正在加载中....",//加载数据的提示消息
		pagination:true,//显示分页栏
		pageList:[1,2,3,5,10,20,30,50],//设置每一页可以显示几条数据
		pageSize:5,//默认每一页显示几条数据
		rownumbers:true,//显示每一行的行号
		columns:[[
		    {"field":"id","title":"编号","width":130,"align":"center",checkbox:true},
			{"field":"name","title":"姓名","width":130,"align":"center",sortable:true},	
		    {"field":"gender","title":"性别","width":130,"align":"center",sortable:true},
		    {"field":"clazz","title":"班级","width":130,"align":"center",sortable:true},
		    {"field":"score","title":"成绩","width":130,"align":"center",sortable:true},
		    {"field":"result","title":"考核结果","width":130,"align":"center",formatter:function(val,row,index){
		    	//三个参数，分别是:具体的值,(行)list中的每一个map,当前这个map在集合中的下标
		    	if(row.score>=90){
		    		return "<span style='color:green'><b>优秀</b></span>";
		    	}else if(row.score>=80){
		    		return "<span style='color:blue'><b>良好</b></span>";
		    	}else if(row.score>=60){
		    		return "<span style='color:gray'><b>合格</b></span>";
		    	}else{
		    		return "<span style='color:red'><b>不及格</b></span>";
		    	}
		    }},
		    {"field":"bir","title":"出生日期","width":200,"align":"center",sortable:true}
		]]
	});
	
})
