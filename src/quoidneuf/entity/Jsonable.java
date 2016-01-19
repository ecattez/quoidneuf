package quoidneuf.entity;

import javax.xml.bind.annotation.XmlRootElement;

import quoidneuf.util.Json;

/**
 * Un Jsonable est un objet pouvant être représenté sous la forme JSON.
 */
@XmlRootElement
public abstract class Jsonable {
	
	@Override
	public String toString() {
		return Json.getInstance().write(this);
	}

}
