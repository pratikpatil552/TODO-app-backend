package com.pratik.api;

import com.pratik.model.Task;
import com.pratik.util.DatabaseHelper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Path("tasks")
public class TaskResource
{
    private final DatabaseHelper databaseHelper;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public TaskResource (DatabaseHelper databaseHelper)
    {
        this.databaseHelper = databaseHelper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTasks ()
    {
        List<Task> tasks = new ArrayList<>();
        try
        {
            connection = databaseHelper.getConnection();
            String query = "select * from tasks";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Task task = new Task();
                task.setTaskId(resultSet.getInt("task_id"));
                task.setDescription(resultSet.getString("description"));
                task.setStartDate(resultSet.getDate("start_date"));
                task.setTargetDate(resultSet.getDate("target_date"));

                String statusString = resultSet.getString("status");
                task.setStatus(Task.Status.valueOf(statusString));

                tasks.add(task);

            }
            return Response.ok(tasks).build();

        }
        catch (Exception e)
        {
            // Step 8: Handle any exceptions and return HTTP 500
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        finally
        {
            // Step 9: Close all resources (connection, statement, result set)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                // Log the exception (optional)
            }
        }
    }

    @GET
    @Path("/{taskId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getATask (@PathParam("taskId") int taskId)
    {
        try
        {
            connection = databaseHelper.getConnection();
            String query = "select * from tasks WHERE task_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1,taskId);
            resultSet = statement.executeQuery();
            Task task = new Task();

            if (resultSet.next())
            {
                task = new Task();
                task.setTaskId(resultSet.getInt("task_id"));
                task.setDescription(resultSet.getString("description"));
                task.setStartDate(resultSet.getDate("start_date"));
                task.setTargetDate(resultSet.getDate("target_date"));

                String statusString = resultSet.getString("status");
                task.setStatus(Task.Status.valueOf(statusString));

                return Response.ok(task).build();
            }
            else
            {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Task with id " + taskId + " not found.")
                        .build();
            }

        }
        catch (Exception e)
        {
            // Step 8: Handle any exceptions and return HTTP 500
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        finally
        {
            // Step 9: Close all resources (connection, statement, result set)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                // Log the exception (optional)
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createATask (Task task)
    {
        try
        {
            connection = databaseHelper.getConnection();
            String query = "INSERT INTO tasks (description, start_date, target_date, status) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,task.getDescription());
            statement.setDate(2,new java.sql.Date(task.getStartDate().getTime()));
            statement.setDate(3,new java.sql.Date(task.getTargetDate().getTime()));
            statement.setString(4, task.getStatus().name());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0)
            {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next())
                {
                    int taskId = generatedKeys.getInt(1);
                    task.setTaskId(taskId);
                }
                return Response.status(Response.Status.CREATED).entity(task).build(); // Return the created task
            }
            else
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create task.")
                        .build();
            }
        }
        catch (Exception e)
        {
            // Step 8: Handle any exceptions and return HTTP 500
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        finally
        {
            // Step 9: Close all resources (connection, statement, result set)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                // Log the exception (optional)
            }
        }
    }

    @PATCH
    @Path("/{taskId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateATask (@PathParam("taskId") int taskId, Task task)
    {
        try
        {
            connection = databaseHelper.getConnection();
            StringBuilder query = new StringBuilder("UPDATE tasks SET ");
            boolean firstField = true;

            if(task.getDescription() != null)
            {
                query.append("description = ?");
                firstField = false;
            }
            if(task.getStartDate() != null)
            {
                if(!firstField) query.append(", ");
                query.append("start_date = ?");
                firstField = false;
            }
            if (task.getTargetDate() != null)
            {
                if (!firstField) query.append(", ");
                query.append("target_date = ?");
                firstField = false;
            }
            if (task.getStatus() != null)
            {
                if (!firstField) query.append(", ");
                query.append("status = ?");
            }
            query.append(" WHERE task_id = ?");
            statement = connection.prepareStatement(query.toString());
            int paramIndex = 1;

            if (task.getDescription() != null) {
                statement.setString(paramIndex++, task.getDescription());
            }
            if (task.getStartDate() != null) {
                statement.setDate(paramIndex++, new java.sql.Date(task.getStartDate().getTime()));
            }
            if (task.getTargetDate() != null) {
                statement.setDate(paramIndex++, new java.sql.Date(task.getTargetDate().getTime()));
            }
            if (task.getStatus() != null) {
                statement.setString(paramIndex++, task.getStatus().name());
            }
            statement.setInt(paramIndex, taskId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return Response.ok().entity("Task updated successfully.").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Task not found.").build();
            }
        }
        catch (Exception e)
        {
            // Step 8: Handle any exceptions and return HTTP 500
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        finally
        {
            // Step 9: Close all resources (connection, statement, result set)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                // Log the exception (optional)
            }
        }
    }


    @DELETE
    @Path("/{taskId}")
    public Response removeATask (@PathParam("taskId") int taskId)
    {
        try
        {
            connection = databaseHelper.getConnection();
            String query = "DELETE FROM tasks WHERE task_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1,taskId);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0)
            {
                return Response.ok("Task with task id " + taskId + " deleted successfully.").build();
            }
            else
            {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Task with task id " + taskId + " not found.").build();
            }

        }
        catch (Exception e)
        {
            // Step 8: Handle any exceptions and return HTTP 500
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        finally
        {
            // Step 9: Close all resources (connection, statement, result set)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                // Log the exception (optional)
            }
        }
    }


}
