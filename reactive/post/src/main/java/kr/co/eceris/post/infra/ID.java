package kr.co.eceris.post.infra;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ID implements Identifiable<UUID> {

    private UUID uuid;

    @Override
    public UUID getId() {
        return uuid;
    }

    public static ID newID() {
        return new ID(UUID.randomUUID());
    }

    public static ID fromString(String uuid) {
        return new ID(UUID.fromString(uuid));
    }
}
