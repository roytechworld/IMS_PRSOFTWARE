package com.prsoftwares.util;

import java.sql.SQLException;
import java.util.ArrayList;


import java.util.List;
import java.util.StringTokenizer;

import com.prsoftwares.dao.DaoFactory;
import com.prsoftwares.dao.SupplierMasterDAO;
import com.prsoftwares.dto.SupplierMasterDTO;

public class DummyDB {
	SupplierMasterDTO sdto=new SupplierMasterDTO();
	List<SupplierMasterDTO> slist=new ArrayList<SupplierMasterDTO>();
	private int totalCountries;
	private String data = "Afghanistan,	Albania, Algeria, Andorra, Angola, Antigua & Deps,"+ 
		"Argentina,	Armenia, Australia,	Austria,Azerbaijan,Bahamas,Bahrain,Bangladesh,Barbados,"+ 
		"Belarus,Belgium,Belize,Benin,Bhutan,Bolivia,Bosnia Herzegovina,Botswana,Brazil,Brunei,"+ 
		"Bulgaria,Burkina,Burundi,Cambodia,Cameroon,Canada,Cape Verde,Central African Rep,Chad,"+ 
		"Chile,China,Colombia,Comoros,Congo,Congo {Democratic Rep},Costa Rica,Croatia,Cuba,Cyprus,"+ 
		"Czech Republic,Denmark,Djibouti,Dominica,Dominican Republic,East Timor,Ecuador,Egypt,El Salvador,"+ 
		"Equatorial Guinea,	Eritrea,Estonia,Ethiopia,Fiji,Finland,France,Gabon,Gambia,Georgia,Germany,"+ 
		"Ghana,	Greece,	Grenada,Guatemala,Guinea,Guinea-Bissau,Guyana,Haiti,Honduras,Hungary,Iceland,"+ 
		"India,	Indonesia,Iran,Iraq,Ireland {Republic},Israel,Italy,Ivory Coast,Jamaica,Japan,"+ 
		"Jordan,Kazakhstan,Kenya,Kiribati,Korea North,Korea South,Kosovo,Kuwait,Kyrgyzstan,Laos,"+ 
		"Latvia,Lebanon,Lesotho,Liberia,Libya,Liechtenstein,Lithuania,Luxembourg,Macedonia,Madagascar,"+ 
		"Malawi,Malaysia,Maldives,Mali,Malta,Marshall Islands,Mauritania,Mauritius,Mexico,Micronesia,"+ 
		"Moldova,Monaco,Mongolia,Montenegro,Morocco,Mozambique,Myanmar, {Burma},Namibia,Nauru,Nepal,"+ 
		"Netherlands,New Zealand,Nicaragua,Niger,Nigeria,Norway,Oman,Pakistan,Palau,Panama,Papua New Guinea,"+ 
		"Paraguay,Peru,Philippines,Poland,Portugal,Qatar,Romania,Russian Federation,Rwanda,St Kitts & Nevis,"+
		"St Lucia,Saint Vincent & the Grenadines,Samoa,San Marino,Sao Tome & Principe,Saudi Arabia,Senegal,"+ 
		"Serbia,Seychelles,Sierra Leone,Singapore,Slovakia,Slovenia,Solomon Islands,Somalia,South Africa,"+ 
		"Spain,Sri Lanka,Sudan,Suriname,Swaziland,Sweden,Switzerland,Syria,Taiwan,Tajikistan,Tanzania, "+
		"Thailand,Togo,Tonga,Trinidad & Tobago,Tunisia,Turkey,Turkmenistan,Tuvalu,Uganda,Ukraine,United Arab Emirates,"+
		"United Kingdom,United States,Uruguay,Uzbekistan,Vanuatu,Vatican City,Venezuela,Vietnam,Yemen,Zambia,Zimbabwe";
	private List<String> countries   =new ArrayList<String>();
	public DummyDB() {
		
		
		
		countries = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(data, ",");
		DaoFactory dfact=new DaoFactory();
		SupplierMasterDAO sdao=dfact.CreateSupplierManager();
		try {
			slist=sdao.getAllSupplierDetails();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<slist.size();i++)
		{
			
			countries.add(slist.get(i).getSUPPLIERNAME());
			
		}
		
	
//		while(st.hasMoreTokens()) {
//			
//			String suppliername=slist.get(0).getSUPPLIERNAME();
//			countries.add(st.nextToken().trim());
//		}
		totalCountries = countries.size();
	}
	
	public List<String> getData(String query) {
		
		String country = null;
		query = query.toLowerCase();
		List<String> matched = new ArrayList<String>();
		for(int i=0; i<totalCountries; i++) {
			country = countries.get(i).toLowerCase();
			System.out.println(country);
			if(country.startsWith(query)) {
				matched.add(countries.get(i));
			}
		}
		return matched;
	}
}
