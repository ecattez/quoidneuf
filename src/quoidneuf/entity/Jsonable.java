package quoidneuf.entity;

import javax.xml.bind.annotation.XmlRootElement;

import quoidneuf.util.Json;

@XmlRootElement
public abstract class Jsonable {
	
	public String toString() {
		return Json.getInstance().write(this);
	}

}
