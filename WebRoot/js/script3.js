$(function(){
	//1、获得要渲染成图表的组件
	var mydiv = $("#mydiv3")[0];
	
	//2、初始化组件
	var chart = echarts.init(mydiv);
	
	//3、设置图表的属性
	var option={
		
		title:{
			text:"数据汇总",
			subtext:"统计人:张三",
			x:"center"
		},	
		legend:{
			orient: 'vertical',
			data:[],//需要一个一维数组，显示每一个板块，是什么名称
			x:"left"
		},   
		tooltip:{
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c}人({d}%)"
		},//鼠标悬停时的，提示信息
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {
	                show: true,
	                type: ['pie', 'funnel']
	            },
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    itemStyle: {
	        normal: {
	            // 阴影的大小
	            shadowBlur: 200,
	            // 阴影水平方向上的偏移
	            shadowOffsetX: 0,
	            // 阴影垂直方向上的偏移
	            shadowOffsetY: 0,
	            // 阴影颜色
	            shadowColor: 'rgba(0, 0, 0, 0.5)'
	        }
	    },
		 series : [
		           {
		               name: '学员信息汇总',
		               type: 'pie',
		               radius: '55%',
		               data:[]  //需要一个二维数组，用于显示数据
		           }
		       ]
	};
	
	//4、将设置好的属性与初始化以后的组件关联
	chart.setOption(option);
	/*****************************以下代码，动态读取数据************************************************************************/
	
	$.getJSON("ajax?method=loadPie",function(data){
		chart.setOption({
			legend:{
				data:data.titles
			},  
			 series : [
			           {
			               name: '学员信息汇总',
			               type: 'pie',
			               radius: '55%',
			               data:data.list
			           }
			   ]
		});
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
});