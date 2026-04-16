import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "@/stores/authStore";
import { useWorkspaceStore } from "@/stores/workspaceStore";
import { workspaceService } from "@/services/workspaceService";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Kanban, LogOut, Plus, LayoutGrid } from "lucide-react";
import type { WorkspaceRole } from "@/types";

const roleBadgeVariant = (role: WorkspaceRole) => {
  switch (role) {
    case "Owner": return "default";
    case "Member": return "secondary";
    case "Viewer": return "outline";
  }
};

const DashboardPage = () => {
  const navigate = useNavigate();
  const { user, logout, isAuthenticated } = useAuthStore();
  const { workspaces, setWorkspaces, addWorkspace } = useWorkspaceStore();
  const [newName, setNewName] = useState("");
  const [open, setOpen] = useState(false);

  useEffect(() => {
    if (!isAuthenticated) { navigate("/"); return; }
    workspaceService.getWorkspaces().then(setWorkspaces);
  }, [isAuthenticated, navigate, setWorkspaces]);

  const handleCreate = () => {
    if (!newName.trim() || !user) return;
    addWorkspace({
      id: `w${Date.now()}`,
      name: newName.trim(),
      ownerId: user.id,
      members: [{ user, role: "Owner" }],
      boardCount: 0,
    });
    setNewName("");
    setOpen(false);
  };

  const handleLogout = () => { logout(); navigate("/"); };

  const getUserRole = (wId: string): WorkspaceRole => {
    const ws = workspaces.find((w) => w.id === wId);
    const member = ws?.members.find((m) => m.user.id === user?.id);
    return member?.role ?? "Viewer";
  };

  return (
    <div className="min-h-screen bg-background">
      <header className="flex items-center justify-between border-b border-border px-6 py-4">
        <div className="flex items-center gap-3">
          <div className="gradient-primary flex h-8 w-8 items-center justify-center rounded-lg">
            <Kanban className="h-4 w-4 text-primary-foreground" />
          </div>
          <span className="text-lg font-semibold text-foreground">FlowBoard</span>
        </div>
        <div className="flex items-center gap-3">
          <Avatar className="h-8 w-8 border border-border">
            <AvatarFallback className="bg-secondary text-secondary-foreground text-xs">
              {user?.name?.split(" ").map((n) => n[0]).join("") ?? "?"}
            </AvatarFallback>
          </Avatar>
          <span className="text-sm text-muted-foreground hidden sm:inline">{user?.name}</span>
          <Button variant="ghost" size="icon" onClick={handleLogout} className="text-muted-foreground hover:text-foreground">
            <LogOut className="h-4 w-4" />
          </Button>
        </div>
      </header>

      <main className="mx-auto max-w-5xl p-6">
        <div className="mb-8 flex items-center justify-between">
          <h2 className="text-2xl font-bold text-foreground">Workspaces</h2>
          <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
              <Button className="gradient-primary text-primary-foreground shadow-glow">
                <Plus className="mr-2 h-4 w-4" /> New Workspace
              </Button>
            </DialogTrigger>
            <DialogContent className="bg-card border-border">
              <DialogHeader>
                <DialogTitle>Create Workspace</DialogTitle>
              </DialogHeader>
              <div className="space-y-4 pt-2">
                <div className="space-y-2">
                  <Label>Workspace name</Label>
                  <Input placeholder="My Workspace" value={newName} onChange={(e) => setNewName(e.target.value)} />
                </div>
                <Button onClick={handleCreate} className="w-full gradient-primary text-primary-foreground">Create</Button>
              </div>
            </DialogContent>
          </Dialog>
        </div>

        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {workspaces.map((ws, i) => (
            <Card
              key={ws.id}
              className="cursor-pointer border-border bg-card transition-colors hover:bg-surface-hover"
              style={{ animationDelay: `${i * 50}ms` }}
              onClick={() => navigate(`/workspaces/${ws.id}`)}
            >
              <CardHeader className="flex flex-row items-start justify-between pb-2">
                <CardTitle className="text-base font-semibold text-foreground">{ws.name}</CardTitle>
                <Badge variant={roleBadgeVariant(getUserRole(ws.id))} className="text-xs">
                  {getUserRole(ws.id)}
                </Badge>
              </CardHeader>
              <CardContent>
                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                  <LayoutGrid className="h-3.5 w-3.5" />
                  <span>{ws.boardCount} board{ws.boardCount !== 1 ? "s" : ""}</span>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </main>
    </div>
  );
};

export default DashboardPage;
