package SS1.app;

import utils.TextIO;

import java.io.FileInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

public class MyDbApp {
	private Properties props;
	private Connection connect;

	public MyDbApp() // constructor
    {
		this.loadProps();
		this.connect();
	}

    /**
     * @effects: load the properties file
     * @return:
     *          if load done
     *              return true
     *          else
     *              return false
     */
	public boolean loadProps()
    {
		Properties property = new Properties();
		try {
			FileInputStream fileInputStream = new FileInputStream("src/SS1/app/postgres.properties");
			property.load(fileInputStream);
			this.props = property;
			return true;
		} catch (Exception e) {
			e.getMessage();
			return false;
		}
	}

    /**
     * @effects: connect to the database by using the properties file
     * @return:
     *          if connect not null
     *              return true
     *          else
     *              reuturn fasle
     */
	public boolean connect() {
		if (this.connect != null) {

			return true;
		}
		try {
			String url = "jdbc:" + this.props.getProperty("driver") + "://"
					             + this.props.getProperty("host") + ":"
					             + this.props.getProperty("port") + "/"
				                 + this.props.getProperty("dbname");
			this.connect = DriverManager.getConnection(url,
					        this.props.getProperty("user"),
					        this.props.getProperty("pass"));
			return true;
		} catch (SQLException e) {
			System.err.println("Got an exception");
			e.printStackTrace();
			return false;
		}
	}

    /**
     * @effects: close the connection
     */
    public void close() {
        if (this.connect != null)
            try {
                this.connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        this.connect = null;
    }

    /**
     * @effects delete <b>some</b> data rows from a table using DELETE statement <tt>sql</tt>.
     *  <p><pre>If succeeded
     *    return true
     *  else
     *    display an error message
     *    return false</pre>
     */
    public boolean delete(String sql) {
        Statement s = null;
        try {
            s = connect.createStatement();
            s.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.err.println("Error: failed to delete data from table");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (s != null)
                    s.close();
            } catch (SQLException e) {
                e.getMessage();
            }
        }
    }

    /**
     * @effects update data in a table using UPDATE statement <tt>sql</tt>.
     *  <p><pre>If succeeded
     *    return true
     *  else
     *    display an error message
     *    return false</pre>
     *
     */
    public boolean update(String sql) {
        Statement s = null;
        try {
            s = connect.createStatement();
            s.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.err.println("Error: failed to execute UPDATE statement");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (s != null)
                    s.close();
            } catch (SQLException e) {
                e.getMessage();
            }
        }
    }

    /**
     * @effects insert data into a table using INSERT statement <tt>sql</tt>.
     *  <p><pre>If succeeded
     *    return true
     *  else
     *    display an error message
     *    return false</pre>
     */
	public boolean insert(String sql) {
	    Statement s = null;
	    try {
	      s = connect.createStatement();
	      s.executeUpdate(sql);
	      return true;
	    } catch (SQLException e) {
	      System.err.println("Error: failed to execute INSERT statement");
	      e.printStackTrace();
	      return false;
	    } finally {
	      try {
	        if (s != null) 
	          s.close(); 
	      } catch (SQLException e) {
	        e.getMessage();
	      }
	    }
	  }

    /**
     * @effects  columns name of table split by ","
     * @throws SQLException
     */
	public String[] getColumnName(String tableName) throws SQLException {
		String a="";

		DatabaseMetaData meta = this.connect.getMetaData();
		ResultSet rs = meta.getColumns(null, null, tableName, null);
		while (rs.next())
            a += (rs.getString("COLUMN_NAME") + ",");
        rs.close();
        return a.split(",");
	}

    /**
     * @param tableName
     * @return String[][] with data are name, type, size of database
     * @throws SQLException
     */
    public String[][] getColumnProp(String tableName) throws SQLException {

        Statement st;
        ResultSet rs;
        st = this.connect.createStatement();

        rs = st.executeQuery("SELECT * FROM " + tableName);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        String[] size = new String[columnCount];
        String[] name = new String[columnCount];
        String[] type = new String[columnCount];

        for (int i = 1; i <= columnCount; i++) {
            if (size[i-1] == null) 	size[i-1] = "";
            size[i-1]+= rsmd.getColumnDisplaySize(i);

            if (name[i-1] == null) 	name[i-1] = "";
            name[i-1]+= rsmd.getColumnName(i);

            if (type[i-1] == null) 	type[i-1] = "";
            type[i-1]+= rsmd.getColumnTypeName(i);
        } //
        String[][] properties;
        properties = new String[][]{name, type, size};
        rs.close();
        st.close();
        return properties;

    }

    /**
     * @param str
     * @return
     *          if str is null
     *              return false
     *          if length of str is 0
     *              return false
     *          if first charater of str is -
     *              return false
     *          else
     *              return true
     */
    public boolean isInteger(String str) {
    if (str == null) {
        return false;
    }
    int length = str.length();
    if (length == 0) {
        return false;
    }
    int i = 0;
    if (str.charAt(0) == '-') {
        if (length == 1) {
            return false;
        }
        i = 1;
    }
    for (; i < length; i++) {
        char c = str.charAt(i);
        if (c <= '/' || c >= ':') {
            return false;
        }
    }
    return true;
    }

    /**
     *
     * @param tableName
     * @param columnName
     * @return maximum length of column from table
     * @throws SQLException
     */
    public int getMaxLength(String tableName, String columnName) throws SQLException
    {
        Statement st=this.connect.createStatement();
        ResultSet rs;
        int max=0;
        rs = st.executeQuery("SELECT "+ columnName +" FROM "+tableName);
        ResultSetMetaData metaData = rs.getMetaData();
        while (rs.next())
        {
            for(int i =1;i<=metaData.getColumnCount();++i)
            {
                if(max<rs.getString(i).length()&&rs.getString(i).length()>getColumnName(tableName)[i].length())
                {
                    max = rs.getString(i).length();
                }
                if(max<getColumnName(tableName)[i].length()&&rs.getString(i).length()<getColumnName(tableName)[i].length())
                {
                    max= getColumnName(tableName)[i].length();
                }
            }
        }
        return max;
    }

    /**
     * @effects use to display all the data to console into a table
     * @param tableName
     * @param list
     * @throws SQLException
     */
    public void displayConsole(String tableName,ArrayList<Integer> list) throws SQLException
    {
        ResultSet rs = null;
        Statement st = this.connect.createStatement();

        if(tableName.equals("student") || tableName.equals("course")) {
            rs = st.executeQuery("SELECT * FROM " + tableName + " ORDER BY " + tableName + "id");
        }
        if(tableName.equals("enrolment"))
        {
            rs = st.executeQuery("SELECT * FROM " + tableName + " ORDER BY student");
        }
        int S=0;
        for (Integer aList : list) {
            S += aList + ("   ".length() * 2) + "|".length();
        }
        for(int j=0;j<=S;j++)
        {
            TextIO.put("=");
        }
        TextIO.putln();

        for(int j=0;j<Math.floor(S/2)-((tableName.toUpperCase()+" TABLE").length()/2);j++)
        {
            TextIO.put(" ");
        }
        TextIO.put(tableName.toUpperCase()+" TABLE");
        for(int j=0;j<Math.ceil(S/2)-((tableName.toUpperCase()+" TABLE").length()/2);j++)
        {
            TextIO.put(" ");
        }

        TextIO.putln();

        for(int j=0;j<=S;j++)
        {
            TextIO.put("=");
        }
        TextIO.putln();
        TextIO.put("|");
        for(int j = 0; j <list.size();j++){
            TextIO.put("   ");
            TextIO.putf("%-" + list.get(j)+"."+ list.get(j) + "s", getColumnName(tableName)[j]);
            TextIO.put("   ");
            TextIO.put("|");
        }
        TextIO.putln();
        for(int j=0;j<=S;j++)
        {
            TextIO.put("-");
        }
        TextIO.putln();

        assert rs != null;
        while (rs.next())
        {
            TextIO.put("|");

            for(int j = 1; j <list.size();j++){
                TextIO.put("   ");
                TextIO.putf("%-" + list.get(j - 1) + "s", rs.getString(j));
                TextIO.put("   ");
                TextIO.put("|");
            }
            TextIO.put("   ");
            TextIO.putf("%-" + list.get(list.size() - 1) + "s", rs.getString(list.size()));
            TextIO.put("   ");
            TextIO.put("|");
            TextIO.putln();
        }
        for(int j=0;j<=S;j++)
        {
            TextIO.put("=");
        }
        TextIO.putln();
        rs.close();
        st.close();
    }

    /**
     * @effects  use to wriwrite the data to HTML
     * @param sql
     * @param filename
     * @throws SQLException
     */
    public void writetoHTML(String sql, String filename) throws SQLException{
        Statement st = this.connect.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        int count = metaData.getColumnCount();
        String result = "<meta charset=\"UTF-8\"><table border=1>";
        for (int i = 1; i <= count; i++) {
            result += "<th>";
            result += metaData.getColumnLabel(i);
            result += "</th>";
        }
        result += "</tr> \n";
        while (rs.next()) {
            result += "<tr>";
            for (int i = 1; i <= count; i++) {
                result += "<td>";
                result += rs.getString(i);
            }
            result += "</tr> \n";
        }
        result += "</table> \n";
        String userDir = System.getProperty("user.dir");
        String fileChar = System.getProperty("file.separator");
        String file = userDir+fileChar+ "src"+"\\"+"SS1"+"\\"+"app"+"\\"+ filename+".html";
        TextIO.putln("Written result to file " + file);
        TextIO.writeFile(file);
        TextIO.putln(result);
        TextIO.writeStandardOutput();
    }

    /**
     * @param input
     * @param column
     * @param tableName
     * @return
     *          if input is ""
     *              return false
     *          if tableName is  not enrolment
     *              if  input is not id from database
     *                  return false
     *           if the size of input bigger than size of database
     *              return false
     *           if the id from student table negative and is a string
     *              return false
     *           if input not the type dd/mm/yyyy
     *              return false
     *           if input semester not from 1 to 8
     *              return false
     *           if final grade not P, G, E or F
     *              return false
     *           else
     *              return true
     * @throws SQLException
     */
    public boolean validateAdd(String input, String column, String tableName) throws SQLException {
        Statement st = this.connect.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM "+tableName );
        String[] columns = this.getColumnName(tableName);
        String[][] prop = this.getColumnProp(tableName);
        // properties[0]=name;
        // properties[1]=type;
        // properties[2]=size;
        if(input.equalsIgnoreCase(""))
        {
            System.err.println("You have to input something");
            return false;
        }
        if(!tableName.equalsIgnoreCase("enrolment"))
        {
            while (rs.next()) {
                if (column.equals(prop[0][0])) {
                    if (input.equalsIgnoreCase(rs.getString(1))) {
                        System.err.println("Already exist " + prop[0][0]);
                        return false;
                    }
                }
            }
        }
        for (int i = 0; i < columns.length; i++) {
            if(column.equalsIgnoreCase(prop[0][i])){
                // check size (length)
                if (Integer.parseInt(prop[2][i]) < input.length()) {
                    System.err.println("Input length of " + prop[0][i]
                            + " is not valid, max lenghth is "
                            + Integer.parseInt(prop[2][i]));
                    return false;
                }

                // check type
                if(prop[1][i].equalsIgnoreCase("int4")){
                    if(this.isInteger(input)){
                        int num = Integer.parseInt(input);
                        if(num<0){
                            System.err.println(prop[0][i]+ " cannot be negative");
                            return false;
                        }
                    }else{
                        System.err.println("You must input an integer");
                        return false;
                    }
                }
                // check date format
                if(prop[0][i].equalsIgnoreCase("dateofbirth")){
                    SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
                    java.util.Date date;
                    try {
                        date = sdf.parse(input);
                        if (input.equals(sdf.format(date))) {
                            return true;
                        }
                    }
                    catch(Exception e) {
                        System.err.println("You must input right date of birth and in form date/month/year");
                    }
                    return false;
                }
                if(prop[0][i].equalsIgnoreCase("semester")){
                    if(this.isInteger(input)){
                        int num = Integer.parseInt(input);
                        if(num<0){
                            System.err.println(prop[0][i]+ " cannot be negative");
                            return false;
                        }
                        if(num<1||num>8){
                            System.err.println(prop[0][i]+ " must be from 1 to 8");
                            return false;
                        }
                    }else{
                        System.err.println("You must input an integer");
                        return false;
                    }
                }
                if(prop[0][i].equalsIgnoreCase("finalgrade")){
                    if((!input.equalsIgnoreCase("F"))&&(!input.equalsIgnoreCase("P"))&&(!input.equalsIgnoreCase("G"))&&(!input.equalsIgnoreCase("E")))
                    {
                        System.err.println(prop[0][i]+ " must be F or P or G or E");
                        return false;
                    }
                }
            }
        }
        if(tableName.equalsIgnoreCase("enrolment")) {
            if(prop[0][0].equalsIgnoreCase("student")) {
                rs = st.executeQuery("SELECT * FROM student");
                while (rs.next()) {
                    if (input.equalsIgnoreCase(rs.getString(1))) {
                        return true;
                    }
                }
            }
            if(prop[0][1].equalsIgnoreCase("course")) {
                rs = st.executeQuery("SELECT * FROM course");
                while (rs.next()) {
                    if (input.equals(rs.getString(1))) {
                        return true;
                    }
                }
            }
            for (int i = 0; i < columns.length; i++) {
                if (column.equalsIgnoreCase(prop[0][i])) {
                    if(prop[0][i].equalsIgnoreCase("finalgrade")){
                        if((input.equalsIgnoreCase("F"))||(input.equalsIgnoreCase("P"))||(input.equalsIgnoreCase("G"))||(input.equalsIgnoreCase("E")))
                        {
                            return true;
                        }
                    }
                    if(prop[0][i].equalsIgnoreCase("semester")){
                        if(this.isInteger(input)){
                            int num = Integer.parseInt(input);
                            if(num>=1&&num<=8){
                                return true;
                            }
                        }
                    }
                }
            }
            System.err.println("Does not exist");
            return false;
        }
        return true;
    }

    /**
     * @param input
     * @param tableName
     * @return
     *          if input not in database
     *              return true
     *          else
     *              return false
     * @throws SQLException
     */
    public boolean validateDelete(String input,String tableName) throws SQLException
    {
        Statement st = this.connect.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM "+tableName );

            while(rs.next()) {
                if(input.equalsIgnoreCase(rs.getString(1))){
                    return true;
                }
            }
        System.err.println("ID does not exist");
        return false;
    }

    /**
     * @param input
     * @param column
     * @param tableName
     * @return
     *          if input in the type d/m/yyyy
     *              return true
     *          else
     *              return false
     * @throws SQLException
     */
    public boolean validateEdit(String input,String column, String tableName) throws SQLException
    {

        String[][] prop = this.getColumnProp(tableName);
        String[] columns = this.getColumnName(tableName);

            for (int i = 1; i < columns.length; i++) {
                if(column.equalsIgnoreCase(prop[0][i])) {
                    if (prop[0][i].equalsIgnoreCase("dateofbirth")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
                        java.util.Date date;
                        try {
                            date = sdf.parse(input);
                            if (input.equals(sdf.format(date))) {
                                return true;
                            }
                        } catch (Exception e) {
                            System.err.println("You must input right date of birth and in form date/month/year");
                        }
                        return false;
                    }
                }
            }
        return true;
    }

    /**
     * @param id
     * @param input
     * @param column
     * @param tableName
     * @return
     *          the old data when input ""
     * @throws SQLException
     */
    public String getString(String id,String input,String column,String tableName) throws SQLException
    {
        Statement st = this.connect.createStatement();
        ResultSet rs;
        if(!tableName.equalsIgnoreCase("enrolment")) {
            rs = st.executeQuery("SELECT " + column + " FROM " + tableName + " WHERE " + tableName + "id =" + "'" +id+ "'");
        }else {
            rs = st.executeQuery("SELECT " + column + " FROM " + tableName + " WHERE " + tableName + " =" + id);
        }
        ResultSetMetaData metaData = rs.getMetaData();
        while (rs.next())
        {
            for(int i =1;i<=metaData.getColumnCount();++i)
            {
                    if (input.equalsIgnoreCase("")) {
                        input += rs.getString(i);
                    }
            }
        }
        return input;
    }

    /**
     * @param input
     * @param tableName
     * @return
     *          if id input in the database
     *              return true
     *          else
     *              return false
     * @throws SQLException
     */
    public boolean validateInput(String input,String tableName) throws SQLException
    {
        Statement st = this.connect.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM "+tableName);
        while (rs.next()) {
            if (input.equalsIgnoreCase(rs.getString(1))) {
                return true;
            }
        }
        System.out.println("Does not exist");
        return false;
    }

    /**
     * @param id
     * @param input
     * @param column
     * @param tableName
     * @return
     *          if grade is F, P, G, E
     *              return true
     *          if course in database
     *              return true
     *          if semester in database
     *              return true
     *          else
     *              return false
     * @throws SQLException
     */
    public boolean validateGrade(String id,String input,String column,String tableName) throws SQLException
    {
        Statement st = this.connect.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM "+tableName+" WHERE student ="+id);

        if(column.equalsIgnoreCase("finalgrade")) {
            if((input.equalsIgnoreCase("F"))||(input.equalsIgnoreCase("P"))||(input.equalsIgnoreCase("G"))||(input.equalsIgnoreCase("E")))
            {
                return true;
            }
        }
        if(column.equalsIgnoreCase("course")){
            while (rs.next()) {
                if (input.equalsIgnoreCase(rs.getString(2))) {
                    return true;
                }
            }
        }
        if(column.equalsIgnoreCase("semester")){
            while (rs.next()) {
                if (input.equalsIgnoreCase(rs.getString(3))) {
                    return true;
                }
            }
        }
        System.out.println("Does not exist");
        return false;
    }
}
