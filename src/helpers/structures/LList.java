package helpers.structures;

import helpers.functions.FileHelper;
import helpers.functions.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.AppData;

/**
 * Created by Святослав on 09.10.2016.
 */
public class LList {
	private ObservableList<WList> lists = FXCollections.observableArrayList();

	public boolean add(WList l) {
		System.out.println("adding..." + l.print());
		for (int i = 0; i < lists.size(); i++)
			if (lists.get(i).getName().equals(l.getName())) {
				Helper.showError("Error in LList.add(WList l)\r\nWList with name " + l.getName() + " was already added");
				return false;
			}
		lists.add(l);
		if (FileHelper.isReading()) return true;
		else return FileHelper.rewrite();
	}

	public void insert(int index, WList list) {
		lists.add(index, list);
		FileHelper.rewrite();
	}

	public ObservableList<WList> getLists() {
		return lists;
	}

	public WList get(int i) {
		return lists.get(i);
	}

	public WList getListFromAll(String name){																			//start search from 0 (include All Words)
		for (int i = 0; i < lists.size(); i++)
			if (lists.get(i).getName().equals(name)) return lists.get(i);
		Helper.showError("Error in LList.getList(String name)\r\nCan't find list with name " + name);
		return null;
	}

	public WList getList(String name) {																					//start search from 1
		for (int i = 1; i < lists.size(); i++)
			if (lists.get(i).getName().equals(name)) return lists.get(i);
		Helper.showError("Error in LList.getList(String name)\r\nCan't find list with name " + name);
		return null;
	}

	public WList getList(int key) {
		for (int i = 1; i < lists.size(); i++)
			if (lists.get(i).getKey() == key) return lists.get(i);
		Helper.showError("Error in LList.getList(int key)\r\nCan't find list with key " + key);
		return null;
	}

	public void remove(int key) {
		for(int i = 0;i<lists.size();++i)if(lists.get(i).getKey() ==  key){
			lists.remove(i);
			AppData.revalidKeys();
			return;
		}
		Helper.showError("Error in LList.remove(int key)\nList with key: " + key + " doesn't available");
	}

	public int find(String name, WList wList) {																			// search index of list with name 'name' BUT NO 'wlist.getName()'. Returns -1 if not found
		String wName = "";
		if(wList != null)wName = wList.getName();
		for (int i = 0; i < lists.size(); i++)
			if (lists.get(i).getName().equals(name) && !lists.get(i).getName().equals(wName)) return i;
		return -1;
	}

	public void set(WList wList){
		for(int i = 0;i<lists.size();++i)if(wList.getKey() == lists.get(i).getKey()){
			lists.set(i, wList);
			return;
		}
		Helper.showError("Error in LList.set(WList wList)\nWList with key + " + wList.getKey() + " doesn't exist");
	}
}
