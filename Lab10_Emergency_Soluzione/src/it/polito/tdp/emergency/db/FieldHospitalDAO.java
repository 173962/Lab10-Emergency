////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Field Hospital DAO                                          //
//   #     _\  // Test with MariaDB 10 on win                                 //
//   |   _/    //                                                             //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.emergency.model.Arrival;
import it.polito.tdp.emergency.model.Patient;

public class FieldHospitalDAO {

	public void GetPatient(Patient p) {

		final String sql = "SELECT nome FROM patients WHERE id = ?";

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, p.getId());
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				p.setName(rs.getString("name"));
				p.setStatus(rs.getString("status"));
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore query!");
		}
	}

	public Map<Integer, Patient> GetAllPatients() {

		final String sql = "SELECT * FROM patients";
		Map<Integer, Patient> patients = new HashMap<Integer, Patient>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Patient tp = new Patient(rs.getInt("id"), rs.getString("name"));
				patients.put(rs.getInt("id"), tp);
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore query!");
		}
		return patients;
	}

	public List<Arrival> GetAllArrivals() {

		final String sql = "SELECT * FROM arrivals ORDER BY timestamp ASC";
		List<Arrival> arrivals = new ArrayList<Arrival>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				arrivals.add(new Arrival(rs.getTimestamp("timestamp"), rs.getInt("patient"), rs.getString("triage")));
			}
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore query!");
		}
		return arrivals;
	}
}
