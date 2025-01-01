package com.pratik.api;

import com.pratik.model.Task;
import com.pratik.util.DatabaseHelper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

            while (resultSet.next()) {
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

        } catch (Exception e) {
            // Step 8: Handle any exceptions and return HTTP 500
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } finally {
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
