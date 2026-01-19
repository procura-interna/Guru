package pt.procurainterna.guru.cdi;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.time.LocalDate;
import net.dv8tion.jda.api.JDA;
import org.apache.hc.client5.http.classic.HttpClient;
import org.jdbi.v3.core.Jdbi;

import com.google.inject.AbstractModule;

import pt.procurainterna.guru.HttpClientProvider;
import pt.procurainterna.guru.leetcode.DailyChallengeFetcher;
import pt.procurainterna.guru.leetcode.HttpClientDailyChallengeFetcher;
import pt.procurainterna.guru.leetcode.JdaLeetcodeChallengePoster;
import pt.procurainterna.guru.leetcode.LeetcodeChallengePoster;
import pt.procurainterna.guru.persistance.DailyChallengeConfigRepository;
import pt.procurainterna.guru.persistance.DbInitializer;
import pt.procurainterna.guru.persistance.DefaultDbInitializer;
import pt.procurainterna.guru.persistance.GuildInitialRoleRepository;
import pt.procurainterna.guru.persistance.JdbiDailyChallengeConfigRepository;
import pt.procurainterna.guru.persistance.JdbiGuildInitialRoleRepository;

public class GuruCdiModule extends AbstractModule {

  private final Jdbi jdbi;
  private final JDA jda;

  public GuruCdiModule(Jdbi jdbi, JDA jda) {
    this.jdbi = jdbi;
    this.jda = jda;
  }

  @Override
  protected void configure() {

    bind(Jdbi.class).toInstance(jdbi);
    bind(JDA.class).toInstance(jda);
    bind(Gson.class).toInstance(gson());
    bind(GuildInitialRoleRepository.class).to(JdbiGuildInitialRoleRepository.class);
    bind(DbInitializer.class).to(DefaultDbInitializer.class);
    bind(HttpClient.class).toProvider(HttpClientProvider.class).in(Singleton.class);
    bind(DailyChallengeConfigRepository.class).to(JdbiDailyChallengeConfigRepository.class);
    bind(DailyChallengeFetcher.class).to(HttpClientDailyChallengeFetcher.class);
    bind(LeetcodeChallengePoster.class).to(JdaLeetcodeChallengePoster.class);
  }

  private Gson gson() {
    final GsonBuilder builder = new GsonBuilder();

    builder.registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
      @Override public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) { out.nullValue(); return; }
        out.value(value.toString()); // 2026-01-19
      }
      @Override public LocalDate read(JsonReader in) throws IOException {
        switch (in.peek()) {
          case NULL -> { in.nextNull(); return null; }
          default -> { return LocalDate.parse(in.nextString()); }
        }
      }
    });

    return builder.create();
  }

}
