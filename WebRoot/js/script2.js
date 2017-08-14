$(function(){
	
	//获得组件
	var mydiv = $("#mydiv2")[0];
	
	//初始化组件
	var chart = echarts.init(mydiv); 
	
	//设置图表的相关属性配置
   var option={
		   
		title:{
			text:"学员信息汇总",
			subtext:"统计人:张三",
			x:"center"
		},   
		legend:{
			data:["考核结果"],//对针对每一种颜色的说明信息,该值必须与series中的name值一样，才可以显示
			x:"left"
		},    
		tooltip:{},//鼠标悬停时的，提示信息
	    toolbox: { //工具栏
	        show : true,
	        feature : {
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
		xAxis:{
			data:[],//指定每一列的标题//需要动态获得------一维数组
		},
		yAxis:{},
		//用于设置要显示的数据有哪些   
		series:{
			name:"考核结果",//图表的名称
			type:"bar",//图表的类型  bar:柱状图  pie:饼图   line折线图
			data:[] //要显示的数据，必须是数组:柱状图：一维数组---动态获取 
			
		}
   };

	//将设置的属性与初始化以后的组件关联
    chart.setOption(option);
	
    /***********前面显示图标的结构，后面用来加载数据，显示结构中********************************************************************/
    $.getJSON("ajax?method=showBar",function(data){
    	
    	chart.setOption({
    		xAxis:{
    			data:data.keys//指定每一列的标题//需要动态获得------一维数组
    		},
    		series:{
    			name:"考核结果",//图表的名称
    			type:"bar",//图表的类型  bar:柱状图  pie:饼图   line折线图
    			data:data.vals//要显示的数据，必须是数组:柱状图：一维数组---动态获取 
    			
    		}
    	});
    	
    });
    
    
    
    
    
    
    
    
    
    
    
    
    
    
});