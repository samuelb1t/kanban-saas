import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuthStore } from "@/stores/authStore";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { workspaceService } from "@/services/workspaceService";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tooltip, TooltipContent, TooltipTrigger } from "@/components/ui/tooltip";
import { ArrowLeft, Plus, Columns3 } from "lucide-react";

const WorkspacePage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const { currentWorkspace, setCurrentWorkspace, boards, setBoards, addBoard } = useWorkspaceStore();
  const [newName, setNewName] = useState("");
  const [open, setOpen] = useState(false);

  useEffect(() => {
    if (!isAuthenticated) { navigate("/"); return; }
    if (id) {
      workspaceService.getWorkspace(id).then((ws) => setCurrentWorkspace(ws ?? null));
      workspaceService.getBoards(id).then(setBoards);
    }
  }, [id, isAuthenticated, navigate, setCurrentWorkspace, setBoards]);

  const handleCreate = () => {
    if (!newName.trim() || !id) return;
    addBoard({
      id: `b${Date.now()}`,
      workspaceId: id,
      name: newName.trim(),
      columns: [],
    });
    setNewName("");
    setOpen(false);
  };

  return (
    <div className="min-h-screen bg-background">
      <header className="flex items-center gap-4 border-b border-border px-6 py-4">
        <Button variant="ghost" size="icon" onClick={() => navigate("/dashboard")} className="text-muted-foreground hover:text-foreground">
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <h1 className="text-lg font-semibold text-foreground">{currentWorkspace?.name ?? "Workspace"}</h1>
      </header>

      <main className="mx-auto max-w-5xl p-6">
        {/* Members */}
        {currentWorkspace && (
          <div className="mb-8">
            <h3 className="mb-3 text-sm font-medium text-muted-foreground">Members</h3>
            <div className="flex -space-x-2">
              {currentWorkspace.members.map((m) => (
                <Tooltip key={m.user.id}>
                  <TooltipTrigger>
                    <Avatar className="h-8 w-8 border-2 border-background">
                      <AvatarFallback className="bg-secondary text-secondary-foreground text-xs">
                        {m.user.name.split(" ").map((n) => n[0]).join("")}
                      </AvatarFallback>
                    </Avatar>
                  </TooltipTrigger>
                  <TooltipContent>{m.user.name} · {m.role}</TooltipContent>
                </Tooltip>
              ))}
            </div>
          </div>
        )}

        {/* Boards */}
        <div className="mb-6 flex items-center justify-between">
          <h2 className="text-2xl font-bold text-foreground">Boards</h2>
          <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
              <Button className="gradient-primary text-primary-foreground shadow-glow">
                <Plus className="mr-2 h-4 w-4" /> New Board
              </Button>
            </DialogTrigger>
            <DialogContent className="bg-card border-border">
              <DialogHeader>
                <DialogTitle>Create Board</DialogTitle>
              </DialogHeader>
              <div className="space-y-4 pt-2">
                <div className="space-y-2">
                  <Label>Board name</Label>
                  <Input placeholder="Sprint Board" value={newName} onChange={(e) => setNewName(e.target.value)} />
                </div>
                <Button onClick={handleCreate} className="w-full gradient-primary text-primary-foreground">Create</Button>
              </div>
            </DialogContent>
          </Dialog>
        </div>

        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {boards.map((board, i) => (
            <Card
              key={board.id}
              className="cursor-pointer border-border bg-card transition-colors hover:bg-surface-hover animate-fade-in"
              style={{ animationDelay: `${i * 50}ms` }}
              onClick={() => navigate(`/boards/${board.id}`)}
            >
              <CardHeader className="pb-2">
                <CardTitle className="text-base font-semibold text-foreground">{board.name}</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                  <Columns3 className="h-3.5 w-3.5" />
                  <span>{board.columns.length} column{board.columns.length !== 1 ? "s" : ""}</span>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </main>
    </div>
  );
};

export default WorkspacePage;
