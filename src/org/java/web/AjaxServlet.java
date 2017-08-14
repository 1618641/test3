package org.java.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONObject;

import org.java.dao.AjaxDao;
import org.java.dao.AjaxDao2;
import org.java.dao.AjaxDao3;

public class AjaxServlet extends HttpServlet {

	private AjaxDao dao = new AjaxDao();
	private AjaxDao2 dao2 = new AjaxDao2();
	private AjaxDao3 dao3 = new AjaxDao3();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String method = request.getParameter("method");
		if (method.equals("load"))
			load(request, response);
		if (method.equals("add"))
			add(request, response);
		if (method.equals("del"))
			del(request, response);
		if (method.equals("update"))
			update(request, response);
		if (method.equals("showBar"))
			showBar(request, response);
		if (method.equals("loadPie"))
			loadPie(request, response);
		if (method.equals("export"))
			export(request, response);
	}

	public void loadPie(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		// 需要得到一个一维数组，还需要得到一个二维数组
		List<Map<String, Object>> list = dao3.getList();// 二维数组

		List<Object> titles = new ArrayList<Object>();// 存放所有的标题---一维数组
		// 得到所有Map的键的名称
		for (Map<String, Object> m : list) {
			titles.add(m.get("name"));// 把每一个name放到集合中
		}
		JSONObject json = new JSONObject();
		json.put("list", list);
		json.put("titles", titles);
		out.write(json.toString());

		out.flush();
		out.close();
	}

	public void showBar(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		// 得到集合
		Map<String, Object> map = dao2.getBar();

		JSONObject json = new JSONObject();
		// 得到键的集合
		Set<String> keys = map.keySet();
		// 得到所有值的集合
		Collection<Object> vals = map.values();

		json.put("keys", keys);
		json.put("vals", vals);

		out.write(json.toString());

		out.flush();
		out.close();
	}

	/**
	 * 修改数据
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer id = Integer.parseInt(request.getParameter("id"));
		Integer score = Integer.parseInt(request.getParameter("score"));
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String clazz = request.getParameter("clazz");
		String bir = request.getParameter("bir");

		dao.update(id, name, gender, clazz, score, bir);

	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ids = request.getParameter("ids");
		String[] allId = ids.split(",");
		for (String id : allId) {
			dao.del(Integer.parseInt(id));
		}

	}

	/**
	 * 添加数据
	 */
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 获得表单中的数据，添加以数据库中
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String clazz = request.getParameter("clazz");
		Integer score = Integer.parseInt(request.getParameter("score"));
		String bir = request.getParameter("bir");

		dao.add(name, gender, clazz, score, bir);

	}

	/**
	 * 加载数据
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		// 取查询参数,但要注意：第一次加载时，由于没有点击查询按钮,如果这四个参数是没有值的，结果为null
		String name = request.getParameter("name");
		String clazz = request.getParameter("clazz");
		String gender = request.getParameter("gender");
		String result = request.getParameter("result");

		System.out.println(name + "\t" + clazz + "\t" + gender + "\t" + result);

		// 取得当前页，以及每一页显示的条数
		Integer page = Integer.parseInt(request.getParameter("page"));// 当前页
		Integer rows = Integer.parseInt(request.getParameter("rows"));// 每一页显示几条

		// 取排序的字段，注意第一次，一定是null，只有点击了排序后，才会有值
		String sort = request.getParameter("sort");// 按哪一个字段排序
		String order = request.getParameter("order");// 排序方式

		// 要使用json，返回两个值： 总数，二维数组
		List<Map<String, Object>> list = dao.load(page, rows, sort, order,
				name, clazz, gender, result);

		// 把查询到的结果，保存在session，可以在导出的时候使用
		request.getSession().setAttribute("list", list);

		JSONObject json = new JSONObject();
		json.put("rows", list);// 存放二维数组
		json.put("total", dao.getCount(name, clazz, gender, result));// 暂时这样写，但这种写法，不能分页

		out.write(json.toString());

		out.flush();
		out.close();
	}

	public void export(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("##############################################################");
		// 下载的文件名称
		String fname = "数据报表.xls";

		// 中文转码
		String fileName = URLEncoder.encode(fname, "utf-8");

		// 显示下载excel文件的窗体
		response.setContentType("application/ms-vnd.excel");
		// 设置下载面板中显示哪些内容
		response.setHeader("Content-disposition", "attachment;fileName="
				+ fileName);

		// 得到一个输出流，用于将电子文档，输出到客户端
		OutputStream out = response.getOutputStream();

		/************************** 下面的代码拷贝上一章的练习即可 ******************************************************************************/

		try {
			// 创建一个可以用流进行输出 的电子表格文档(工作簿)
			WritableWorkbook wb = Workbook.createWorkbook(out);

			// 创建工作簿中，可以用流输出 的表单
			WritableSheet st = wb.createSheet("Y2J45班", 0);// 表单的名称，下标为几的表单

			/******************************** 产生表单中的内容 *******************************************/

			// 指定文档的格式
			st.getSettings().setDefaultColumnWidth(20);// 设置列宽

			// 创建一个字体，用于指定哪一种字体输出
			WritableFont ft = new WritableFont(WritableFont.ARIAL, 20,
					WritableFont.BOLD);// 字体类型，大小，是否加粗

			// 设置单元格显示的格式
			WritableCellFormat wcf = new WritableCellFormat(ft);// 参数是:字体
			wcf.setBorder(Border.ALL, BorderLineStyle.THIN);// 显示哪一个位置的边框,边框是样式粗边框还是细边框
			wcf.setAlignment(Alignment.CENTRE);// 内容居中对齐

			// 添加大标题
			Label title = new Label(0, 0, "J45班信息汇总", wcf);
			st.addCell(title);// 添加到表单

			st.mergeCells(0, 0, 5, 0);// 合并单元格(开始列下标，开始行下标，结束 列下标，结束 行下标)

			// 表格中的数据，都是以Label(标签 )方式来呈现
			Label labId = new Label(0, 1, "编号", wcf);// 列下标，行下标,要显示的内容,采用哪一种单元格样式
			Label labName = new Label(1, 1, "姓名", wcf);// 列下标，行下标,要显示的内容
			Label labGender = new Label(2, 1, "性别", wcf);// 列下标，行下标,要显示的内容
			Label labClazz = new Label(3, 1, "班级", wcf);// 列下标，行下标,要显示的内容
			Label labScore = new Label(4, 1, "成绩", wcf);// 列下标，行下标,要显示的内容
			Label labBir = new Label(5, 1, "出生日期", wcf);// 列下标，行下标,要显示的内容

			// 将label添加到表单中
			st.addCell(labId);
			st.addCell(labName);
			st.addCell(labGender);
			st.addCell(labClazz);
			st.addCell(labScore);
			st.addCell(labBir);

			// 从session中取得所有数据
			List<Map<String, Object>> list = (List<Map<String, Object>>) request
					.getSession().getAttribute("list");

			for (int i = 0; i < list.size(); i++) {

				Map<String, Object> m = list.get(i);// 每一个map，就是一条数据
				Label id = new Label(0, i + 2, m.get("id").toString(), wcf);
				Label name = new Label(1, i + 2, m.get("name").toString(), wcf);
				Label gender = new Label(2, i + 2, m.get("gender").toString(),
						wcf);
				Label clazz = new Label(3, i + 2, m.get("clazz").toString(),
						wcf);
				Label score = new Label(4, i + 2, m.get("score").toString(),
						wcf);
				Label bir = new Label(5, i + 2, m.get("bir").toString(), wcf);

				st.addCell(id);
				st.addCell(name);
				st.addCell(gender);
				st.addCell(clazz);
				st.addCell(score);
				st.addCell(bir);
			}

			/********************************************************************************************/
			// 输出文档
			wb.write();

			// 关闭工作簿与输出流
			wb.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
