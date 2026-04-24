package EchoFeed.UserBot.Creater;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BotUser {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  private String name;
  private String persona_description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersona_description() {
        return persona_description;
    }

    public void setPersona_description(String persona_description) {
        this.persona_description = persona_description;
    }
}
