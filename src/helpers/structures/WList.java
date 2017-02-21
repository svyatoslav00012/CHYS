package helpers.structures;

import helpers.functions.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.AppData;

import java.util.Random;

public class WList {
	private int key;
	private String name;
	private ObservableList<Word> words = FXCollections.observableArrayList();

	public WList(int key, String name) {
		this.key = key;
		this.name = name;
	}

	public WList() {
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObservableList<Word> getWords() {
		return words;
	}

	public Word getWord(int key) {
		for (int i = 0; i < words.size(); ++i) {
			if (words.get(i).getKey() == key) {
				return words.get(i);
			}
		}
		Helper.showError("Error in WList.getWord(int key)\r\nCan't find key " + key);
		return null;
	}

	public Word get(int i) {
		return words.get(i);
	}

	public void set(Word w) {
		for (int i = 0; i < words.size(); i++) {
			if (words.get(i).getKey() == w.getKey()) {
				words.set(i, w);
				System.out.println("setTitledPane");
				return;
			}
		}
	}

	public void clone(WList w) {
		key = w.getKey();
		name = w.getName().toString();
		words = copy(w.getWords());
	}

	public WList makeDuplicate(){																						// make a dublicate of list:
		WList list = new WList();																						//	same words but name + number, new key
		list.setKey(AppData.getFreeListKey());																			//
		list.getWords().setAll(words);
		int num = getNumberAtTheEnd(name);
		num++;
		int l = name.lastIndexOf('(');
		if(l == -1)l = name.length();
		while(AppData.getLists().find(name.substring(0, l) + "(" + num + ")", null) != -1)num++;
		list.name = name.substring(0, l) + "(" + num + ")";
		return list;
	}

	public boolean add(Word w) {
		words.add(w);
		return true;
	}

	public boolean add(int key) {
		for (int i = 0; i < words.size(); ++i) {
			if (words.get(i).getKey() == key) {
				Helper.showError("Error adding to Wlist: " + key + ", word: " + key + " was already added!");
				return false;
			}
		}
		words.add(AppData.getLists().get(0).getWord(key));
		return true;
	}

	public void remove(Word w) {
		for (int i = 0; i < words.size(); i++)
			if (words.get(i).equals(w)) {
				AppData.getWordKeys()[w.getKey()] = false;
				words.remove(i);
				return;
			}
	}

	public void remove(int key) {
		for (int i = 0; i < words.size(); i++)
			if (words.get(i).getKey() == key) {
				words.remove(i);
				return;
			}
		Helper.log("Error in WList.remove(int key)\r\nCan't find word with key " + key);
	}

	public ObservableList<Word> getSubList(String s) {
		s = Helper.getCleanString(s.toLowerCase());
		ObservableList<Word> ans = FXCollections.observableArrayList();
		for (int i = 0; i < words.size(); i++) {
			String w1 = Helper.getCleanString(words.get(i).getEng().toLowerCase());
			String w2 = Helper.getCleanString(words.get(i).get(AppData.getSettings().getTran()).toLowerCase());
			if (w1.indexOf(s) != -1 || w2.indexOf(s) != -1) ans.add(words.get(i));
		}
		return ans;
	}

	public ObservableList<Word> getSubList(String s, ObservableList<Word> anotherWords) {
		s = s.toLowerCase();
		ObservableList<Word> ans = FXCollections.observableArrayList();
		for (int i = 0; i < words.size(); i++) {
			String w1 = Helper.getCleanString(words.get(i).getEng().toLowerCase());
			String w2 = Helper.getCleanString(words.get(i).get(AppData.getSettings().getTran()).toLowerCase());
			if ((w1.indexOf(s) != -1 || w2.indexOf(s) != -1) && !words.get(i).inside(anotherWords))
				ans.add(words.get(i));
		}
		return ans;
	}

	public ObservableList<Word> copy(ObservableList<Word> list) {
		ObservableList<Word> words = FXCollections.observableArrayList();
		for (int i = 0; i < list.size(); i++) {
			words.add(new Word(list.get(i).getKey(), list.get(i).getEng().toString(), list.get(i).getRus().toString(), list.get(i).getUkr().toString()));
		}
		return words;
	}

	public boolean isEqual(WList another) {
		if (!name.equals(another.getName()) || key != another.getKey() || words.size() != another.getWords().size())
			return false;
		for (int i = 0; i < words.size(); i++)
			if (words.get(i).getKey() != another.get(i).getKey()) return false;
		return true;
	}

	public int find(String eng, String rus, String ukr) {
		for (int i = 0; i < words.size(); i++)
			if (words.get(i).getEng().toLowerCase().equals(eng.toLowerCase())
					&& words.get(i).getRus().toLowerCase().equals(rus.toLowerCase()) && words.get(i).getUkr().toLowerCase().equals(ukr.toLowerCase()))
				return i;
		return -1;
	}

	public void mix() {
		ObservableList<Word> a = FXCollections.observableArrayList();
		ObservableList<Word> b = FXCollections.observableArrayList();
		a = copy(words);
		Random rand = new Random();
		for (int i = 0; i < words.size(); i++) {
			if (a.size() < 1) break;
			int r = rand.nextInt(a.size());
			b.add(a.get(r));
			a.remove(r);
		}
		words = copy(b);
	}

	public String print() {
		return key + "/" + name;
	}

	public int getWordIndex(int key) {
		for (int i = 0; i < words.size(); ++i)
			if (words.get(i).getKey() == key) return i;
		Helper.showError("Error in +" + name + ".getWordIndex(int key)\nCan't find word with key " + key);
		return -1;
	}

	public int getWordKey(String word) {
		word = Helper.getCleanString(word.toLowerCase());
		for (int i = 0; i < words.size(); ++i) {
			String eng = Helper.getCleanString(words.get(i).getEng().toLowerCase());
			String tran = Helper.getCleanString(words.get(i).get(AppData.getSettings().getTran()).toLowerCase());
			if (word.equals(eng) || word.equals(tran)) return words.get(i).getKey();
		}
		Helper.showError("Error in WList.getWordKey(String word)\n Word " + word + " doesn't exist in this list\nList(name: "+name);
		return -1;
	}

	public int getNumberAtTheEnd(String s){																				// returns a number at the end of list name(or returns 0 if is doesn't exist)
		int l = name.lastIndexOf('(');
		int r = name.lastIndexOf(')');
		int ans = -1;
		if(l != -1 && r != -1 && r-l>1)ans = Integer.parseInt("0"+Helper.getCleanString(name.substring(l+1, r)));
		if(ans < 0)ans = 0;
		return ans;
	}
}
