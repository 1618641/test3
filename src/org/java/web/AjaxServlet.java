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

		// ��Ҫ�õ�һ��һά���飬����Ҫ�õ�һ����ά����
		List<Map<String, Object>> list = dao3.getList();// ��ά����

		List<Object> titles = new ArrayList<Object>();// ������еı���---һά����
		// �õ�����Map�ļ�������
		for (Map<String, Object> m : list) {
			titles.add(m.get("name"));// ��ÿһ��name�ŵ�������
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

		// �õ�����
		Map<String, Object> map = dao2.getBar();

		JSONObject json = new JSONObject();
		// �õ����ļ���
		Set<String> keys = map.keySet();
		// �õ�����ֵ�ļ���
		Collection<Object> vals = map.values();

		json.put("keys", keys);
		json.put("vals", vals);

		out.write(json.toString());

		out.flush();
		out.close();
	}

	/**
	 * �޸�����
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
	 * ɾ��
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
	 * �������
	 */
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ��ñ��е����ݣ���������ݿ���
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String clazz = request.getParameter("clazz");
		Integer score = Integer.parseInt(request.getParameter("score"));
		String bir = request.getParameter("bir");

		dao.add(name, gender, clazz, score, bir);

	}

	/**
	 * ��������
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

		// ȡ��ѯ����,��Ҫע�⣺��һ�μ���ʱ������û�е����ѯ��ť,������ĸ�������û��ֵ�ģ����Ϊnull
		String name = request.getParameter("name");
		String clazz = request.getParameter("clazz");
		String gender = request.getParameter("gender");
		String result = request.getParameter("result");

		System.out.println(name + "\t" + clazz + "\t" + gender + "\t" + result);

		// ȡ�õ�ǰҳ���Լ�ÿһҳ��ʾ������
		Integer page = Integer.parseInt(request.getParameter("page"));// ��ǰҳ
		Integer rows = Integer.parseInt(request.getParameter("rows"));// ÿһҳ��ʾ����

		// ȡ������ֶΣ�ע���һ�Σ�һ����null��ֻ�е��������󣬲Ż���ֵ
		String sort = request.getParameter("sort");// ����һ���ֶ�����
		String order = request.getParameter("order");// ����ʽ

		// Ҫʹ��json����������ֵ�� ��������ά����
		List<Map<String, Object>> list = dao.load(page, rows, sort, order,
				name, clazz, gender, result);

		// �Ѳ�ѯ���Ľ����������session�������ڵ�����ʱ��ʹ��
		request.getSession().setAttribute("list", list);

		JSONObject json = new JSONObject();
		json.put("rows", list);// ��Ŷ�ά����
		json.put("total", dao.getCount(name, clazz, gender, result));// ��ʱ����д��������д�������ܷ�ҳ

		out.write(json.toString());

		out.flush();
		out.close();
	}

	public void export(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("##############################################################");
		// ���ص��ļ�����
		String fname = "���ݱ���.xls";

		// ����ת��
		String fileName = URLEncoder.encode(fname, "utf-8");

		// ��ʾ����excel�ļ��Ĵ���
		response.setContentType("application/ms-vnd.excel");
		// ���������������ʾ��Щ����
		response.setHeader("Content-disposition", "attachment;fileName="
				+ fileName);

		// �õ�һ������������ڽ������ĵ���������ͻ���
		OutputStream out = response.getOutputStream();

		/************************** ����Ĵ��뿽����һ�µ���ϰ���� ******************************************************************************/

		try {
			// ����һ����������������� �ĵ��ӱ���ĵ�(������)
			WritableWorkbook wb = Workbook.createWorkbook(out);

			// �����������У������������ �ı�
			WritableSheet st = wb.createSheet("Y2J45��", 0);// �������ƣ��±�Ϊ���ı�

			/******************************** �������е����� *******************************************/

			// ָ���ĵ��ĸ�ʽ
			st.getSettings().setDefaultColumnWidth(20);// �����п�

			// ����һ�����壬����ָ����һ���������
			WritableFont ft = new WritableFont(WritableFont.ARIAL, 20,
					WritableFont.BOLD);// �������ͣ���С���Ƿ�Ӵ�

			// ���õ�Ԫ����ʾ�ĸ�ʽ
			WritableCellFormat wcf = new WritableCellFormat(ft);// ������:����
			wcf.setBorder(Border.ALL, BorderLineStyle.THIN);// ��ʾ��һ��λ�õı߿�,�߿�����ʽ�ֱ߿���ϸ�߿�
			wcf.setAlignment(Alignment.CENTRE);// ���ݾ��ж���

			// ��Ӵ����
			Label title = new Label(0, 0, "J45����Ϣ����", wcf);
			st.addCell(title);// ��ӵ���

			st.mergeCells(0, 0, 5, 0);// �ϲ���Ԫ��(��ʼ���±꣬��ʼ���±꣬���� ���±꣬���� ���±�)

			// ����е����ݣ�������Label(��ǩ )��ʽ������
			Label labId = new Label(0, 1, "���", wcf);// ���±꣬���±�,Ҫ��ʾ������,������һ�ֵ�Ԫ����ʽ
			Label labName = new Label(1, 1, "����", wcf);// ���±꣬���±�,Ҫ��ʾ������
			Label labGender = new Label(2, 1, "�Ա�", wcf);// ���±꣬���±�,Ҫ��ʾ������
			Label labClazz = new Label(3, 1, "�༶", wcf);// ���±꣬���±�,Ҫ��ʾ������
			Label labScore = new Label(4, 1, "�ɼ�", wcf);// ���±꣬���±�,Ҫ��ʾ������
			Label labBir = new Label(5, 1, "��������", wcf);// ���±꣬���±�,Ҫ��ʾ������

			// ��label��ӵ�����
			st.addCell(labId);
			st.addCell(labName);
			st.addCell(labGender);
			st.addCell(labClazz);
			st.addCell(labScore);
			st.addCell(labBir);

			// ��session��ȡ����������
			List<Map<String, Object>> list = (List<Map<String, Object>>) request
					.getSession().getAttribute("list");

			for (int i = 0; i < list.size(); i++) {

				Map<String, Object> m = list.get(i);// ÿһ��map������һ������
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
			// ����ĵ�
			wb.write();

			// �رչ������������
			wb.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
