package pt.procurainterna.guru;

import com.google.inject.Inject;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import pt.procurainterna.guru.persistance.GuildInitialRoleRepository;

import java.io.IOException;

public class GetChinchillaCommand {

    private final ChinchillaImageProvider imageProvider;

    @Inject
    public GetChinchillaCommand(ChinchillaImageProvider imageProvider) {
        this.imageProvider = imageProvider;
    }


    public void execute(SlashCommandInteractionEvent event, InteractionHook hook) {
        var image = this.imageProvider.getImage();


        try (var imageFile = FileUpload.fromData(image, "chinchilla.jpg")) {
            hook.sendMessage("").addFiles(imageFile).queue(success->{
                try {
                    image.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
