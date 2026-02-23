# Lyra User Guide

**Lyra** is the chatbot that powers this product. Lyra is your personal task management assistant: you chat with Lyra to add todos, deadlines, and events, and to list, mark, update, or find your tasks. Everything happens in a simple chat interface.

---

## Quick Start

1. Ensure you have **Java 11** or above installed.
2. Download the latest `lyra.jar` from the [releases page](#).
3. Run the application with:
   ```
   java -jar lyra.jar
   ```
4. Type a command in the input box and press **Send** (or hit Enter).

Your task data is saved automatically to `data/lyra.txt` in the same directory as the jar.

**How to use this guide:** The table below lists every command. Each section that follows explains one command in detail — format, rules, and examples — so you can use all of Lyra’s features.

---

## Features at a Glance

| Command | What it does |
|---------|-------------|
| `todo` | Add a simple task |
| `deadline` | Add a task with a due date |
| `event` | Add a task with a start and end time |
| `list` | View all your tasks |
| `mark` | Mark a task as done |
| `unmark` | Mark a task as not done |
| `delete` | Remove a task |
| `find` | Filter tasks by type |
| `update` | Edit a task's description or date |
| `bye` | Exit the app |

Commands are **case-insensitive** (e.g. `Todo`, `TODO`, and `todo` all work). Where a command needs a task number, use the **index** from the `list` output (1, 2, 3, …). Dates and times use the format **`d/MM/yyyy HHmm`** (e.g. `2/12/2024 1800`). Do not use the pipe character `|` in task descriptions.

---

## Adding a Todo

Adds a simple task with no date attached. Every todo must have a non-empty description.

**Format:** `todo DESCRIPTION`

**Example:** `todo read book`

---

## Adding a Deadline

Adds a task that must be done by a specific date and time.

**Format:** `deadline DESCRIPTION /by DATE`

- `DATE` must be in the format `d/MM/yyyy HHmm` (e.g. `2/12/2024 1800`)

**Example:** `deadline submit report /by 2/12/2024 1800`

---

## Adding an Event

Adds a task with a start time and an end time.

**Format:** `event DESCRIPTION /from START_DATE /to END_DATE`

- Both dates must be in the format `d/MM/yyyy HHmm`
- The start time must be before the end time

**Example:** `event team meeting /from 2/12/2024 1000 /to 2/12/2024 1200`

---

## Listing All Tasks

Displays every task currently in your list, numbered in order. Use these numbers with `mark`, `unmark`, `delete`, and `update`.

**Format:** `list`

---

## Marking a Task as Done

Marks a task as complete. Use the task number from `list` (e.g. `1` for the first task).

**Format:** `mark INDEX`

**Example:** `mark 2`

---

## Unmarking a Task

Moves a completed task back to not done.

**Format:** `unmark INDEX`

**Example:** `unmark 2`

---

## Deleting a Task

Permanently removes a task from your list. The task number is the one shown by `list`; after deletion, remaining numbers shift.

**Format:** `delete INDEX`

**Example:** `delete 3`

---

## Finding Tasks by Type

Shows only tasks of a given type. Use this to see just your todos, just your deadlines, or just your events.

**Format:** `find TYPE`

- `TYPE` must be one of: `todo`, `deadline`, `event`

**Example:** `find deadline`

---

## Updating a Task

Edits an existing task’s description or date. Use the task number from `list`. You can change only one thing per command. For dates, use the same format as when adding: `d/MM/yyyy HHmm`.

- **Description:** works for any task (todo, deadline, or event).
- **`/by`:** only for deadlines (due date).
- **`/from`** and **`/to`:** only for events (start and end time); start must stay before end.

**Formats:**

| What to update | Format |
|----------------|--------|
| Description (any task type) | `update INDEX /description NEW_DESCRIPTION` |
| Due date (deadline only) | `update INDEX /by NEW_DATE` |
| Start time (event only) | `update INDEX /from NEW_DATE` |
| End time (event only) | `update INDEX /to NEW_DATE` |

**Examples:**

```
update 1 /description finish chapter 3
update 2 /by 5/01/2025 0900
update 3 /from 3/12/2024 1400
update 3 /to 3/12/2024 1600
```

---

## Exiting the App

Closes the application.

**Format:** `bye`

---

## Data Storage

- Tasks are saved automatically to `data/lyra.txt` after every change.
- The file is created for you if it does not exist.
- Do not manually edit `lyra.txt` — corrupted entries will be reported on the next launch and the app will start with an empty list.

---

## Error Handling

Lyra shows a clear error message (prefixed with **Oops!**) when something goes wrong — for example, a missing description, an invalid date format, an out-of-range task number, or adding a duplicate task — and waits for your next command without closing. Fix the input and try again.