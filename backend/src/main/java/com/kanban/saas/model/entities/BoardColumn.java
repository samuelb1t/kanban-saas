package com.kanban.saas.model.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "columns")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardColumn {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @Column(name = "col_order")
  private int order;

  @ManyToOne
  @JoinColumn(name = "board_id")
  private Board board;

  @OneToMany(mappedBy = "column")
  private List<Task> tasks;
}
