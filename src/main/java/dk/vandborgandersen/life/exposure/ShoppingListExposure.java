package dk.vandborgandersen.life.exposure;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import dk.vandborgandersen.auth.exposure.SecureServiceExposure;
import dk.vandborgandersen.auth.model.User;
import dk.vandborgandersen.auth.persistence.UserArchivist;
import dk.vandborgandersen.life.exposure.model.AddShoppingListItemRepresentation;
import dk.vandborgandersen.life.exposure.model.AddShoppingListRepresentation;
import dk.vandborgandersen.life.exposure.model.ShoppingListItemRepresentation;
import dk.vandborgandersen.life.exposure.model.ShoppingListRepresentation;
import dk.vandborgandersen.life.model.ShoppingList;
import dk.vandborgandersen.life.model.ShoppingListItem;
import dk.vandborgandersen.life.persistence.ShoppingListArchivist;

/**
 * REST exposure of shopping lists.
 *
 * @author mortena@gmail.com
 */
@Path("/shoppinglists")
@RolesAllowed({ "SHOPPER", "USER_ADMIN" })
public class ShoppingListExposure extends SecureServiceExposure {
    @Inject
    ShoppingListArchivist shoppingListArchivist;

    @Inject
    RepresentationFactory representationFactory;

    @Inject
    UserArchivist userArchivist;

    @GET
    @Produces("application/hal+json")
    public Response list(@Context SecurityContext securityContext, @Context UriInfo uriInfo) {
        Optional<User> user = userArchivist.findUser(securityContext.getUserPrincipal().getName());
        List<ShoppingList> shoppingLists = shoppingListArchivist.findShoppingLists(user.get());

        UriBuilder getListBuilder = uriInfo.getBaseUriBuilder().path(ShoppingListExposure.class).path(ShoppingListExposure.class, "get");

        Representation representation = representationFactory
            .newRepresentation(uriInfo.getBaseUriBuilder().path(ShoppingListExposure.class).build());
        shoppingLists.stream().forEach(shoppingList -> representation.withBeanBasedRepresentation("shoppingLists",
            getListBuilder.build(shoppingList.getListNameSimplified()).toASCIIString(), new ShoppingListRepresentation(shoppingList)));

        return Response.ok(representation).build();
    }

    @GET
    @Path("{listIdentifier}")
    @Produces("application/hal+json")
    public Response get(@Context SecurityContext securityContext, @Context UriInfo uriInfo,
                        @PathParam("listIdentifier") String listIdentifier) {
        Optional<ShoppingList> list = shoppingListArchivist.getList(listIdentifier);
        if (list.isPresent()) {
            if (!getUserIdentifier(securityContext).equals(list.get().getUserTId())) {
                // Do not expose that the list exists
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            ShoppingList shoppingList = list.get();
            Representation representation = representationFactory.newRepresentation(
                uriInfo.getBaseUriBuilder().path(ShoppingListExposure.class).path(ShoppingListExposure.class, "get")
                    .build(shoppingList.getListNameSimplified()));
            representation.withBean(new ShoppingListRepresentation(shoppingList));

            UriBuilder itemBuilder = uriInfo.getBaseUriBuilder().path(ShoppingListExposure.class).path(ShoppingListExposure.class, "getItem");

            shoppingList.getItemsNotDeleted().stream().forEach(shoppingListItem -> {
                representation.withBeanBasedRepresentation("items",
                    itemBuilder.build(listIdentifier, shoppingListItem.getIdentifier()).toASCIIString(),
                    new ShoppingListItemRepresentation(shoppingListItem));
            });

            return Response.ok(representation).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{listIdentifier}")
    public Response deleteList(@PathParam("relationIdentifier") String relationIdentifier,
                               @PathParam("listIdentifier") String listIdentifier,
                               @Context SecurityContext securityContext) {
        Optional<ShoppingList> list = shoppingListArchivist.getList(listIdentifier);
        if (!list.isPresent() || !list.get().getUserTId().equals(getUser(securityContext).gettId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        shoppingListArchivist.delete(list.get());

        return Response.accepted().build();
    }

    @POST
    @Consumes("application/json")
    public Response addList(@PathParam("relationIdentifier") String relationIdentifier, AddShoppingListRepresentation list,
                            @Context SecurityContext securityContext, @Context UriInfo uriInfo) {
        String listIdentifier =
            shoppingListArchivist.add(userArchivist.findUser(securityContext.getUserPrincipal().getName()).get(), list.getName());

        return Response
            .created(uriInfo.getBaseUriBuilder().path(ShoppingListExposure.class).path(ShoppingListExposure.class, "get").build(listIdentifier))
            .build();
    }

    @GET
    @Path("{listIdentifier}/items")
    @Produces("application/hal+json")
    public Response items(@PathParam("listIdentifier") String listIdentifier) {
        return Response.ok().build();
    }

    @DELETE
    @Path("{listIdentifier}/items/{itemIdentifier}")
    public Response delItem(@PathParam("relationIdentifier") String relationIdentifier,
                            @PathParam("listIdentifier") String listIdentifier,
                            @PathParam("itemIdentifier") String itemIdentifier,
                            @Context SecurityContext securityContext) {
        Optional<ShoppingList> list = shoppingListArchivist.getList(listIdentifier);
        if (!list.isPresent() || !list.get().getUserTId().equals(getUser(securityContext).gettId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<ShoppingListItem> itemForIdentifier = list.get().findItemForIdentifier(itemIdentifier);
        if (itemForIdentifier.isPresent()) {
            itemForIdentifier.get().setDeleted(true);
            shoppingListArchivist.save(list.get());
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.accepted().build();
    }

    @POST
    @Path("{listIdentifier}/items")
    @Consumes("application/json")
    public Response addItem(@PathParam("listIdentifier") String listIdentifier,
                            @Context SecurityContext securityContext,
                            @Context UriInfo uriInfo,
                            AddShoppingListItemRepresentation item) {
        Optional<ShoppingList> list = shoppingListArchivist.getList(listIdentifier);
        if (!list.isPresent() || !list.get().getUserTId().equals(getUser(securityContext).gettId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ShoppingList shoppingList = list.get();
        List<ShoppingListItem> itemsForName = shoppingList.findItemsForName(item.getName());
        ShoppingListItem shoppingListItem = new ShoppingListItem(item.getName(), false, item.getAmount());
        if (itemsForName.size() > 0) {
            shoppingListItem.setInternalCount(itemsForName.size());
        }
        shoppingListItem.initIdentifier();
        shoppingList.addItem(shoppingListItem);
        shoppingListArchivist.save(shoppingList);

        return Response.created(uriInfo.getBaseUriBuilder().path(ShoppingListExposure.class).path(ShoppingListExposure.class, "getItem")
            .build(listIdentifier, shoppingListItem.getIdentifier())).build();
    }

    @GET
    @Path("{listIdentifier}/items/{itemIdentifier}")
    @Produces("application/hal+json")
    public Response getItem(@PathParam("listIdentifier") String listIdentifier, @PathParam("itemIdentifier") String itemIdentifier,
                            @Context SecurityContext securityContext,
                            @Context UriInfo uriInfo) {
        Optional<ShoppingList> list = shoppingListArchivist.getList(listIdentifier);
        if (!list.isPresent() || !list.get().getUserTId().equals(getUser(securityContext).gettId())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<ShoppingListItem> item = list.get().findItemForIdentifier(itemIdentifier);
        if (!item.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ShoppingListItemRepresentation itemRepresentation = new ShoppingListItemRepresentation(item.get());
        Representation representation = representationFactory.newRepresentation(
            uriInfo.getBaseUriBuilder().path(ShoppingListExposure.class).path(ShoppingListExposure.class, "getItem")
                .build(listIdentifier, itemIdentifier)).withBean(itemRepresentation);

        return Response.ok(representation).build();
    }
}
