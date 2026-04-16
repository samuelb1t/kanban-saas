import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { DragDropContext, Droppable, Draggable, type DropResult } from "react-beautiful-dnd";
import { useAuthStore } from "@/stores/authStore";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { workspaceService } from "@/services/workspaceService";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import { ArrowLeft, Plus } from "lucide-react";
import type { Board, Column, Task } from "@/types";

const BoardPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const { currentBoard, setCurrentBoard } = useWorkspaceStore();
  const [addingTaskCol, setAddingTaskCol] = useState<string | null>(null);
  const [taskForm, setTaskForm] = useState({ title: "", description: "" });
  const [newColName, setNewColName] = useState("");
  const [colDialogOpen, setColDialogOpen] = useState(false);

  useEffect(() => {
    if (!isAuthenticated) { navigate("/"); return; }
    if (id) workspaceService.getBoard(id).then((b) => setCurrentBoard(b ?? null));
  }, [id, isAuthenticated, navigate, setCurrentBoard]);

  if (!currentBoard) return <div className="flex min-h-screen items-center justify-center bg-background text-muted-foreground">Loading...</div>;

  const updateBoard = (updater: (b: Board) => Board) => {
    setCurrentBoard(updater(currentBoard));
  };

  const handleDragEnd = (result: DropResult) => {
    const { source, destination } = result;
    if (!destination) return;

    updateBoard((board) => {
      const cols = board.columns.map((c) => ({ ...c, tasks: [...c.tasks] }));
      const srcCol = cols.find((c) => c.id === source.droppableId)!;
      const destCol = cols.find((c) => c.id === destination.droppableId)!;
      const [moved] = srcCol.tasks.splice(source.index, 1);
      moved.columnId = destCol.id;
      destCol.tasks.splice(destination.index, 0, moved);
      return { ...board, columns: cols };
    });
  };

  const addTask = (columnId: string) => {
    if (!taskForm.title.trim()) return;
    updateBoard((board) => ({
      ...board,
      columns: board.columns.map((c) =>
        c.id === columnId
          ? { ...c, tasks: [...c.tasks, { id: `t${Date.now()}`, columnId, title: taskForm.title.trim(), description: taskForm.description.trim(), order: c.tasks.length }] }
          : c
      ),
    }));
    setTaskForm({ title: "", description: "" });
    setAddingTaskCol(null);
  };

  const addColumn = () => {
    if (!newColName.trim()) return;
    updateBoard((board) => ({
      ...board,
      columns: [...board.columns, { id: `c${Date.now()}`, boardId: board.id, name: newColName.trim(), order: board.columns.length, tasks: [] }],
    }));
    setNewColName("");
    setColDialogOpen(false);
  };

  return (
    <div className="flex min-h-screen flex-col bg-background">
      <header className="flex items-center gap-4 border-b border-border px-6 py-4">
        <Button variant="ghost" size="icon" onClick={() => navigate(-1)} className="text-muted-foreground hover:text-foreground">
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <h1 className="text-lg font-semibold text-foreground">{currentBoard.name}</h1>
        <div className="ml-auto">
          <Dialog open={colDialogOpen} onOpenChange={setColDialogOpen}>
            <DialogTrigger asChild>
              <Button variant="outline" size="sm" className="border-border text-muted-foreground hover:text-foreground">
                <Plus className="mr-2 h-3.5 w-3.5" /> Add Column
              </Button>
            </DialogTrigger>
            <DialogContent className="bg-card border-border">
              <DialogHeader><DialogTitle>New Column</DialogTitle></DialogHeader>
              <div className="space-y-4 pt-2">
                <div className="space-y-2">
                  <Label>Column name</Label>
                  <Input placeholder="Review" value={newColName} onChange={(e) => setNewColName(e.target.value)} />
                </div>
                <Button onClick={addColumn} className="w-full gradient-primary text-primary-foreground">Add</Button>
              </div>
            </DialogContent>
          </Dialog>
        </div>
      </header>

      <ScrollArea className="flex-1">
        <DragDropContext onDragEnd={handleDragEnd}>
          <div className="flex gap-4 p-6" style={{ minWidth: "max-content" }}>
            {currentBoard.columns.map((col) => (
              <KanbanColumn key={col.id} column={col} addingTaskCol={addingTaskCol} setAddingTaskCol={setAddingTaskCol} taskForm={taskForm} setTaskForm={setTaskForm} addTask={addTask} />
            ))}
          </div>
        </DragDropContext>
        <ScrollBar orientation="horizontal" />
      </ScrollArea>
    </div>
  );
};

interface KanbanColumnProps {
  column: Column;
  addingTaskCol: string | null;
  setAddingTaskCol: (id: string | null) => void;
  taskForm: { title: string; description: string };
  setTaskForm: (f: { title: string; description: string }) => void;
  addTask: (columnId: string) => void;
}

const KanbanColumn = ({ column, addingTaskCol, setAddingTaskCol, taskForm, setTaskForm, addTask }: KanbanColumnProps) => (
  <div className="flex w-72 flex-shrink-0 flex-col rounded-lg border border-border bg-card">
    <div className="flex items-center justify-between px-4 py-3">
      <div className="flex items-center gap-2">
        <h3 className="text-sm font-semibold text-foreground">{column.name}</h3>
        <span className="text-xs text-muted-foreground">{column.tasks.length}</span>
      </div>
      <Button variant="ghost" size="icon" className="h-6 w-6 text-muted-foreground hover:text-foreground" onClick={() => setAddingTaskCol(column.id)}>
        <Plus className="h-3.5 w-3.5" />
      </Button>
    </div>

    <Droppable droppableId={column.id}>
      {(provided, snapshot) => (
        <div
          ref={provided.innerRef}
          {...provided.droppableProps}
          className={`flex-1 space-y-2 px-3 pb-3 min-h-[60px] transition-colors rounded-b-lg ${snapshot.isDraggingOver ? "bg-surface-hover" : ""}`}
        >
          {column.tasks.map((task, idx) => (
            <TaskCard key={task.id} task={task} index={idx} />
          ))}
          {provided.placeholder}

          {addingTaskCol === column.id && (
            <div className="space-y-2 rounded-md border border-border bg-secondary p-3 animate-fade-in">
              <Input placeholder="Task title" value={taskForm.title} onChange={(e) => setTaskForm({ ...taskForm, title: e.target.value })} className="h-8 text-sm" autoFocus />
              <Textarea placeholder="Description (optional)" value={taskForm.description} onChange={(e) => setTaskForm({ ...taskForm, description: e.target.value })} className="min-h-[60px] text-sm" />
              <div className="flex gap-2">
                <Button size="sm" onClick={() => addTask(column.id)} className="gradient-primary text-primary-foreground text-xs">Add</Button>
                <Button size="sm" variant="ghost" onClick={() => setAddingTaskCol(null)} className="text-xs text-muted-foreground">Cancel</Button>
              </div>
            </div>
          )}
        </div>
      )}
    </Droppable>
  </div>
);

const TaskCard = ({ task, index }: { task: Task; index: number }) => (
  <Draggable draggableId={task.id} index={index}>
    {(provided, snapshot) => (
      <Card
        ref={provided.innerRef}
        {...provided.draggableProps}
        {...provided.dragHandleProps}
        className={`border-border bg-secondary transition-shadow ${snapshot.isDragging ? "shadow-glow" : ""}`}
      >
        <CardContent className="p-3">
          <p className="text-sm font-medium text-foreground">{task.title}</p>
          {task.description && (
            <p className="mt-1 text-xs text-muted-foreground line-clamp-2">{task.description}</p>
          )}
        </CardContent>
      </Card>
    )}
  </Draggable>
);

export default BoardPage;
