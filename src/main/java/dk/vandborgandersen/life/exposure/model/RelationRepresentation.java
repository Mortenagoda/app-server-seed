package dk.vandborgandersen.life.exposure.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A relation between somebody.
 *
 * @author mortena@gmail.com
 */

@XmlRootElement
public class RelationRepresentation {

    private String relationIdentifier;
    private String relationName;
    private List<String> relationEmails;

    public String getRelationIdentifier() {
        return relationIdentifier;
    }

    public void setRelationIdentifier(String relationIdentifier) {
        this.relationIdentifier = relationIdentifier;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public List<String> getRelationEmails() {
        return relationEmails;
    }

    public void setRelationEmails(List<String> relationEmails) {
        this.relationEmails = relationEmails;
    }

}
