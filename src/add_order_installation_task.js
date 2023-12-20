var DivEngineerVisitUtils = Class.create();
DivEngineerVisitUtils.prototype = {
	initialize: function () {
	},

	generateEngineerTasks: function (current) {
		// Check if there are already Engineer tasks related to the probed engineer visit
		var findEngineerTasks = DT.db().query(Tables.EngineerTask, {
			parent: current.getValue('sys_id')
		});

		var engineerTaskCount = findEngineerTasks.getRowCount();

		// If the EVST has no engineer tasks but has a bundle selected it creates the engineer tasks

		if (current.u_engineer_tasks_generated == false && engineerTaskCount == 0 && current.getValue('u_engineer_action_template_bundle') != null) {
			var bundleItems = DT.db().query(Tables.EngineerActionTemplateBundleItem, {
				'u_engineer_action_template_bundel': current.getValue('u_engineer_action_template_bundle')
			}, {
				'order_by': { 'u_order': 'asc' }
			});

			var orderValueCounter = 10;
			while (bundleItems.next()) {

				var engineerActions = DT.db().query(Tables.M2MEngineerAction, {
					'u_engineer_action_template': bundleItems.getValue('u_engineer_action_template')
				}, {
					'order_by': { 'u_order': 'asc' }
				});

				while (engineerActions.next()) {

					var engTsk = DT.db().insert(Tables.EngineerTask, {
						'short_description': engineerActions.u_engineer_action.u_action.getValue(),
						'u_related_engineer_action': engineerActions.u_engineer_action.sys_id.getValue(),
						'order': orderValueCounter,
						'description': engineerActions.u_engineer_action.u_category.u_name.getValue(),
						'priority': current.getValue('priority'),
						'state': '900', //set to new first
						'u_engineer_action': engineerActions.getValue('u_engineer_action'),
						'u_engineer_action_template_bundle_item': bundleItems.getValue('sys_id'),
						'company': current.getValue('company'),
						'u_branch': current.getValue('u_branch'),
						'parent': current.getValue('sys_id')
					});

					orderValueCounter += 10;
				}
			}
			current.setValue('u_engineer_tasks_generated', true);
			current.comments = "Engineer Tasks are generated!";
			current.update();
		}
	},

	generateEngineerTasksCSC: function (csc) {

		/*************
		 * generateEngineerTasksCSC - Generates Engineer Tasks based on Customer Service Components
		 *
		 * @OrderFramework: 1
		 * @param: csc (customer service component object)
		 * @return: comma seperated list of eati sys id's or nothing if not found
		 *
		 *************/

		if (csc) {
			//Retrieve what PDC item the CSC belongs to
			var arrayUtil = new ArrayUtil(); //Needed to use .contains()
			var pdc_item = csc.u_pdc_item.getRefRecord(); //Referenced pdc record from csc
			var pdc_product_number = pdc_item.getValue('u_product_number'); //The product number of the CSC
			var snow_product_type = pdc_item.getValue('u_servicenow_product_type');
			var templates = new GlideRecord(Tables.EAT);// DT.db().query(Tables.EAT,{'u_string_1 !=':''});
			templates.addQuery('u_string_1','!=', '').addOrCondition('u_pdc_list', '!=','');//Only do templates that have values
			templates.query();

			var template = "";
			while( (templates.next()) ){
				var pdc_list = templates.u_pdc_list.toString();
				var arr = []; //Array containing sys_id's of pdc items needed to fetch product numbers
				var pdc_code_list = []; //array containing product numbers of pdc items (for each template)
				if (pdc_list.contains(',')){
					arr = pdc_list.split(',');//Split if multiple
				}
				else if (pdc_list!=""){
					arr.push(templates.u_pdc_list);//Push if singular
				}

				if (pdc_list!="") {
					for(var x in arr){
						var pdc = DT.db().find(Tables.PDC,{sys_id:arr[x]});
						if (pdc!=null)  {
							var u_product_number = pdc.getValue('u_product_number');
							pdc_code_list.push(pdc.getValue('u_product_number'));
						}
					}
					if(arrayUtil.contains(pdc_code_list,pdc_product_number)){
						if (template!="") template+=",";
						template+=templates.getUniqueValue().toString();
					}
				}
				else {
					var type_list = templates.u_string_1.toString();
					var pdc_type_list = []; //array containing product numbers of pdc items (for each template)

					if(type_list.contains(',')){
						pdc_type_list = type_list.split(',');//Split if multiple
					}
					else if (type_list != "") {
						pdc_type_list.push(templates.u_string_1);//Push if singular
					}

					if ( (type_list != "") && (snow_product_type!=null) ) {
						if(arrayUtil.contains(pdc_type_list,snow_product_type)){
							if (template!="") template+=",";
							template+=templates.getUniqueValue().toString();
						}
					}
				}

			}

			if (template!="") {

				var templates_arr = template.split(",");
				var eati_string = "";
				templates_arr.forEach(function(item){
					//Fetch engineer actions to create engineer tasks
					// how : Query the engineer action M2M table and insert a task for each engineer action linked to given EAT
					var engineerActions = DT.db().query(Tables.M2MEngineerAction, {
						'u_engineer_action_template': item
					});

					if(engineerActions.getRowCount() > 0){
						//Create an Engineer Action Template Instance (EATI)
						var eat = DT.db().find(Tables.EAT,{sys_id:item});
						if (eat!=null) var eati_order = eat.getValue('u_order');
						var eati = DT.db().insert(Tables.EATI, {
							'short_description': pdc_item.getDisplayValue(),
							'u_customer_service_component': csc.getUniqueValue(),
							'u_csc_group': csc.getValue('u_csc_group'),
							'company': csc.getValue('company'),
							'u_branch': csc.getValue('u_branch'),
							'order' : eati_order

						});
					}

					while (engineerActions.next()) {
						//Add tasks to the EATI
						var engTsk = DT.db().insert(Tables.EngineerTask, {
							'short_description': engineerActions.u_engineer_action.u_action.getValue(),
							'u_related_engineer_action': engineerActions.u_engineer_action.sys_id.getValue(),
							'description': engineerActions.u_engineer_action.u_category.u_name.getValue(),
							'state': '900', //set to new first
							'u_engineer_action': engineerActions.getValue('u_engineer_action'),
							'u_eati': eati.getUniqueValue(),
							'company': csc.getValue('company'),
							'u_branch': csc.getValue('u_branch'),
							'order': engineerActions.u_engineer_action.u_order
						});
					}

					if (eati.isValidRecord()) {
						if (eati_string!="") eati_string+=",";
						eati_string+=eati.getUniqueValue();
					}
					else {
						//Error handling
						return eati.getLastErrorMessage();
					}
				});
				if (eati_string!="") {
					return eati_string;
				}
				else {
					return; //NO TEMPLATE WAS FOUND return false to prevent billing
				}
			}
			else {
				return;// NO TEMPLATE WAS FOUND return false to prevent billing
			}
		}
	},

	generateEngineerTasksOLI: function (oli) {

		/*************
		 * generateEngineerTasksOLI - Generates Engineer Tasks based on Order Line Items
		 *
		 * @OrderFramework: 2 (CSM)
		 * @param: oli (order line item object)
		 * @return: comma seperated list of eati sys id's or nothing if not found
		 *
		 *************/

		if (oli) {
			//Retrieve what PDC item the CSC belongs to
			var arrayUtil = new ArrayUtil(); //Needed to use .contains()
			var pdc_item = oli.u_pdc_item.getRefRecord(); //Referenced pdc record from csc
			var pdc_product_number = pdc_item.getValue('u_product_number'); //The product number of the CSC
			var snow_product_type = pdc_item.getValue('u_servicenow_product_type');
			var templates = new GlideRecord(Tables.EAT);// DT.db().query(Tables.EAT,{'u_string_1 !=':''});
			templates.addQuery('u_string_1','!=', '').addOrCondition('u_pdc_list', '!=','');//Only do templates that have values
			templates.query();

			var template = "";
			while( (templates.next()) ){
				var pdc_list = templates.u_pdc_list.toString();
				var arr = []; //Array containing sys_id's of pdc items needed to fetch product numbers
				var pdc_code_list = []; //array containing product numbers of pdc items (for each template)
				if (pdc_list.contains(',')){
					arr = pdc_list.split(',');//Split if multiple
				}
				else if (pdc_list!=""){
					arr.push(templates.u_pdc_list);//Push if singular
				}

				if (pdc_list!="") {
					for(var x in arr){
						var pdc = DT.db().find(Tables.PDC,{sys_id:arr[x]});
						if (pdc!=null)  {
							var u_product_number = pdc.getValue('u_product_number');
							pdc_code_list.push(pdc.getValue('u_product_number'));
						}
					}
					if(arrayUtil.contains(pdc_code_list,pdc_product_number)){
						if (template!="") template+=",";
						template+=templates.getUniqueValue().toString();
					}
				}
				else {
					var type_list = templates.u_string_1.toString();
					var pdc_type_list = []; //array containing product numbers of pdc items (for each template)

					if(type_list.contains(',')){
						pdc_type_list = type_list.split(',');//Split if multiple
					}
					else if (type_list != "") {
						pdc_type_list.push(templates.u_string_1);//Push if singular
					}

					if ( (type_list != "") && (snow_product_type!=null) ) {
						if(arrayUtil.contains(pdc_type_list,snow_product_type)){
							if (template!="") template+=",";
							template+=templates.getUniqueValue().toString();
						}
					}
				}

			}

			if (template!="") {

				var templates_arr = template.split(",");
				var eati_string = "";
				templates_arr.forEach(function(item){
					//Fetch engineer actions to create engineer tasks
					// how : Query the engineer action M2M table and insert a task for each engineer action linked to given EAT
					var engineerActions = DT.db().query(Tables.M2MEngineerAction, {
						'u_engineer_action_template': item
					});

					if(engineerActions.getRowCount() > 0){
						//Create an Engineer Action Template Instance (EATI)
						var eat = DT.db().find(Tables.EAT,{sys_id:item});
						if (eat!=null) var eati_order = eat.getValue('u_order');
						var eati = DT.db().insert(Tables.EATI, {
							'short_description': pdc_item.getDisplayValue(),
							'u_order_line_item': oli.getUniqueValue(),
							'u_order_group': oli.getValue('u_order_group'),
							'company': oli.u_order_group.u_account.getValue(),
							'u_branch': oli.u_order_group.u_branch.getValue(),
							'order' : eati_order

						});
					}

					while (engineerActions.next()) {
						//Add tasks to the EATI
						var engTsk = DT.db().insert(Tables.EngineerTask, {
							'short_description': engineerActions.u_engineer_action.u_action.getValue(),
							'u_related_engineer_action': engineerActions.u_engineer_action.sys_id.getValue(),
							'description': engineerActions.u_engineer_action.u_category.u_name.getValue(),
							'state': '900', //set to new first
							'u_engineer_action': engineerActions.getValue('u_engineer_action'),
							'u_eati': eati.getUniqueValue(),
							'company': oli.u_order_group.u_account.getValue(),
							'u_branch': oli.u_order_group.u_branch.getValue(),
							'order': engineerActions.u_engineer_action.u_order
						});
					}

					if (eati.isValidRecord()) {
						if (eati_string!="") eati_string+=",";
						eati_string+=eati.getUniqueValue();
					}
					else {
						//Error handling
						return eati.getLastErrorMessage();
					}
				});
				if (eati_string!="") {
					return eati_string;
				}
				else {
					return; //NO TEMPLATE WAS FOUND return false to prevent billing
				}
			}
			else {
				return;// NO TEMPLATE WAS FOUND return false to prevent billing
			}
		}
	},

	generateEngineerTasksTSM: function (order) {

		/*************
		 * generateEngineerTasksTSM - Generates Engineer Tasks based on Product, Service or Resource Order
		 *
		 * @OrderFramework: TSM
		 * @param: order (customer order object)
		 * @return: comma seperated list of eati sys id's or nothing if not found
		 *
		 *************/

		if (order) {

			// Todo TSM:
			// Determine if Product, Service or Resource Order
			// Check PSR specification on EATI instead of pdc list (check the table: sn_prd_pm_specification and determine the sys class name)
			// check the class name to see what specification it is

			//Retrieve what PDC item the CSC belongs to
			var arrayUtil = new ArrayUtil(); //Needed to use .contains()
			var psr_specification = order.getValue('u_psr_specifications');
			var table = null;
			if(psr_specification == 'Product Specification'){
			    table = 'sn_prd_pm_product_specification_list';
			}else if(psr_specification == 'Service Specification'){
			   table = 'sn_prd_pm_service_specification_list';
			}else if(psr_specification == 'Resource Specification'){
			    table = 'sn_prd_pm_resource_specification_list';
			}


			var psr_item = order.psr_specification.getRefRecord(); //Referenced pdc record from csc
			var psr_product_code = prs_item.getValue('product_code'); //The product number of the CSC
			//var snow_product_type = pdc_item.getValue('u_servicenow_product_type');
			var templates = new GlideRecord(Tables.EAT);// DT.db().query(Tables.EAT,{'u_string_1 !=':''});
			templates.addQuery('u_string_1','!=', '').addOrCondition(table, '!=','');//Only do templates that have values
			templates.query();

			var template = "";
			while( (templates.next()) ){
				var psr_list = templates.u_pdc_list.toString();
				var arr = []; //Array containing sys_id's of pdc items needed to fetch product numbers
				var psr_code_list = []; //array containing product numbers of pdc items (for each template)
				if (psr_list.contains(',')){
					arr = psr_list.split(',');//Split if multiple
				}
				else if (psr_list!=""){
					if(psr_specification_list == 'Product Specification'){
					    arr.push(templates.sn_prd_pm_product_specification_list);//Push if singular
                    }else if(psr_specification_list == 'Service Specification'){
                    	arr.push(templates.sn_prd_pm_service_specification_list);//Push if singular
                    }else if(psr_specification_list == 'Resource Specification'){
                    	arr.push(templates.sn_prd_pm_resource_specification_list);//Push if singular
                    }

				}

				if (psr_list!="") {
					for(var x in arr){
						var psr = DT.db().find(Tables.PDC,{sys_id:arr[x]});
						if (psr!=null)  {
							var product_number = psr.getValue('product_code');
							psr_code_list.push(product_number);
						}
					}
					if(arrayUtil.contains(psr_code_list,psr_product_code)){
						if (template!="") template+=",";
						template+=templates.getUniqueValue().toString();
					}
				}
				else {
					var type_list = templates.u_string_1.toString();
					var psr_type_list = []; //array containing product numbers of pdc items (for each template)

					if(type_list.contains(',')){
						psr_type_list = type_list.split(',');//Split if multiple
					}
					else if (type_list != "") {
						psr_type_list.push(templates.u_string_1);//Push if singular
					}

					if ( (type_list != "") && (snow_product_type!=null) ) {
						if(arrayUtil.contains(pdc_type_list,snow_product_type)){
							if (template!="") template+=",";
							template+=templates.getUniqueValue().toString();
						}
					}
				}

			}

			if (template!="") {

				var templates_arr = template.split(",");
				var eati_string = "";
				templates_arr.forEach(function(item){
					//Fetch engineer actions to create engineer tasks
					// how : Query the engineer action M2M table and insert a task for each engineer action linked to given EAT
					var engineerActions = DT.db().query(Tables.M2MEngineerAction, {
						'u_engineer_action_template': item
					});


					if(engineerActions.getRowCount() > 0){
						//Create an Engineer Action Template Instance (EATI)
						var eat = DT.db().find(Tables.EAT,{sys_id:item});
						if (eat!=null) var eati_order = eat.getValue('u_order');
						var eati = DT.db().insert(Tables.EATI, {
							'short_description': pdc_item.getDisplayValue(),
							'u_psr_specifications': order_u_psr_specifications.getValue(),
							'product_code': psr_product_code,
							'u_project': order.u_project.getValue(),
							'company': order.u_order_group.u_account.getValue(),
							'u_branch': order.u_order_group.u_branch.getValue(),
							'order' : eati_order

						});
					}

					while (engineerActions.next()) {
						//Add tasks to the EATI
						var engTsk = DT.db().insert(Tables.EngineerTask, {
							'short_description': engineerActions.u_engineer_action.u_action.getValue(),
							'u_related_engineer_action': engineerActions.u_engineer_action.sys_id.getValue(),
							'description': engineerActions.u_engineer_action.u_category.u_name.getValue(),
							'state': '900', //set to new first
							'u_engineer_action': engineerActions.getValue('u_engineer_action'),
							'u_eati': eati.getUniqueValue(),
							'company': order.u_order_group.u_account.getValue(),
							'u_branch': order.u_order_group.u_branch.getValue(),
							'order': engineerActions.u_engineer_action.u_order
						});
					}

					if (eati.isValidRecord()) {
						if (eati_string!="") eati_string+=",";
						eati_string+=eati.getUniqueValue();
					}
					else {
						//Error handling
						return eati.getLastErrorMessage();
					}
				});
				if (eati_string!="") {
					return eati_string;
				}
				else {
					return; //NO TEMPLATE WAS FOUND return false to prevent billing
				}
			}
			else {
				return;// NO TEMPLATE WAS FOUND return false to prevent billing
			}
		}
	},


	refQualEngineers: function () {
		var us = ' ';
		var a = current.u_eng_company;

		// return everything if engineer company value is empty
		if (!a)
			return;

		// sys_users from the current u_eng_company and the user has u_is_engineer
		var usr = new GlideRecord('sys_user');
		usr.addQuery('company', a);
		usr.addQuery('u_is_engineer', true);
		usr.query();

		while (usr.next()) {
			if (us.length > 0) {
				//build a comma separated string of users if there is more than one
				us += (',' + usr.sys_id);
			}
			else {
				us = usr.sys_id;
			}
		}

		// return users IN for lists
		return 'sys_idIN' + us;

	},

	refQualEngineerTemplateActionsCollector: function () {
		// Filters all Eng Actions wich are not part of the
		// engineer action template

		var engTemplate = current;
		var act = ' ';

		if (!engTemplate)
			return;

		var gr = new GlideRecord('u_m2m_engineer_act_engineer_act');
		gr.addEncodedQuery('u_engineer_action_template=' + engTemplate.sys_id);
		gr.query();

		while (gr.next()) {
			if (act.length > 0) {
				//build a comma separated string of actions if there is more than one
				act += (',' + gr.u_engineer_action);
			}
			else {
				act = gr.u_engineer_action;
			}
		}

		return 'sys_idNOT IN' + act;
	},

	engineerTasksClosure: function (evst) {
		// Closes all not closed engineer tasks of the current engineer visit

		// Get all engineer tasks where parent is passed in EVST record
		var engTasks = new GlideRecord('u_engineer_task');
		engTasks.addActiveQuery();
		engTasks.addQuery('parent', evst.sys_id);
		engTasks.query();

		while (engTasks.next()) {
			engTasks.state = 4;
			engTasks.close_notes += "\n-- Autoclosed by close button --";
			engTasks.update();
		}

		return true;
	},


	getDSLDataJSON: function (device) {
		// This function accepts an sys_id of a dce device and checks if there is an internetmodule associated
		// If so it returns JSON data to process in the SCC portal


		var jsonData = [];
		var jObj = {};
		var deviceName = '';
		var jsonValue = '[';
		var hasError = false;
		var errorMessage = "";

		// Look up de chosen DCE box.
		var dce = new GlideRecord('u_cmdb_dce');
		dce.addQuery('sys_id', device);

		dce.query();


		if (dce.next()) {
			deviceName = dce.u_display_value;

			// Check if the chosen device has a internet module
			var im = new GlideRecord('u_cmdb_internetmodule');
			im.addQuery('u_cpe', dce.sys_id);

			im.query();

			if (im.next()) {

				// if lowercase acces type is an LTE --> lte then an errormessage occurs asking the user to change to a non LTE device
				var access_type = im.u_access_type.toLowerCase();

				if (access_type == 'lte') {
					errorMessage = "Invalid FRITZ!Box entry. Please select a xDSL- or fibermodem.";
					hasError = true;
				}


				// check values if inside the range of OK ar not OK
				var vectored = im.u_vector;
				var ok;
				if (vectored == "true") {
					ok = (im.u_noise_down >= 5 && im.u_noise_up >= 5 && im.u_att_down <= 55 && im.u_att_up <= 55);
				} else {
					ok = (im.u_noise_down >= 5 && im.u_noise_up >= 5 && im.u_att_down <= 55 && im.u_att_up <= 55);
				}



				// Creating a JSON object to return to requesting code
				jObj = {
					"device_name": dce.u_display_value.toString(),
					"error": hasError,
					"ok": ok,
					"error_message": errorMessage,
					"data": [
						{ "label": "Up Signal-to-Noise Ratio (dB)", "value": im.u_noise_up.toString() },
						{ "label": "Down Signal-to-Noise Ratio (dB)", "value": im.u_noise_up.toString() },
						{ "label": "Up Attenuation (dB)", "value": im.u_att_up.toString() },
						{ "label": "Down Attenuation (dB)", "value": im.u_att_down.toString() },
						{ "label": "Up Speed Max", "value": im.u_speed_up_max.toString() },
						{ "label": "Down Speed Max", "value": im.u_speed_down_max.toString() }
						//{"label":"OK",	"value":ok}
					]
				};
				jsonData.push(jObj);
				return jsonData;

			}
		}
	},

	refQualBranchDSLDevices: function (branch_id) {
		// This reference quaulyfier composes an encoded query
		// to get the correct devices selected with a DSL module

		var sysIDs = '';

		var modules = new GlideRecordSecure('u_cmdb_internetmodule');
		modules.addEncodedQuery('u_branch.sys_id=' + branch_id + '^u_access_type=DSL^install_status!=7^u_cpe.install_status!=7');

		modules.query();

		while (modules.next()) {
			if (sysIDs.length > 0) {
				sysIDs += "," + modules.u_cpe.sys_id;
			}
			else {
				sysIDs += modules.u_cpe.sys_id;
			}

		}
		return "sys_idIN" + sysIDs;
	},

	refQualBranchLTEDevices: function (branch_id) {
		// This reference quaulyfier composes an encoded query
		// to get the correct devices selected with a DSL module

		var sysIDs = '';

		var modules = new GlideRecordSecure('u_cmdb_internetmodule');
		modules.addEncodedQuery('u_branch.sys_id=' + branch_id + '^u_access_type=LTE^install_status!=7^u_cpe.install_status!=7');

		modules.query();

		while (modules.next()) {
			if (sysIDs.length > 0) {
				sysIDs += "," + modules.u_cpe.sys_id;
			}
			else {
				sysIDs += modules.u_cpe.sys_id;
			}

		}
		return "sys_idIN" + sysIDs;
	},

	type: 'DivEngineerVisitUtils'
};
