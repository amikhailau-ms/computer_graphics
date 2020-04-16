import sys
import numpy as np
import pyqtgraph as pg
from PyQt5.QtWidgets import QApplication, QMainWindow, QFileDialog

from WindowUI import Ui_MainWindow
from Utils import *


class MainWindow(QMainWindow, Ui_MainWindow):

    def __init__(self, *args, **kwargs):
        super(MainWindow, self).__init__(*args, **kwargs)
        self.setupUi(self)
        self.setWindowTitle("Lines in a window")
        self.N = 0
        self.lines = []
        self.rectangle = []
        self.all_pen = get_all_qpen()
        self.inside_pen = get_qpen()
        self.init_open()

    def init_open(self):
        self.actionOpen.triggered.connect(self.open_file)
        self.actionExit.triggered.connect(sys.exit)
        pass

    def open_file(self):
        self.N = 0
        self.lines = []
        self.rectangle = []
        self.plot.clear()
        fileName, _ = QFileDialog.getOpenFileName(self, "Open file with data",
                                                  "c:\\", "File with data (*.txt)")
        if fileName:
            all_x = []
            all_y = []
            with open(fileName, 'r') as file:
                self.N = int(next(file))
                for i in range(self.N):
                    x1, y1, x2, y2 = [int(value) for value in file.readline().split()]
                    all_x.extend([x1, x2])
                    all_y.extend([y1, y2])
                    self.lines.append([[x1, y1], [x2, y2]])
                x1, y1, x2, y2 = [int(value) for value in file.readline().split()]
                self.rectangle = [x1, x2, y1, y2]
                all_x.extend([x1, x2])
                all_y.extend([y1, y2])
            self.plot.getPlotItem().vb.setLimits(yMin=np.min(all_y), yMax=np.max(all_y),
                                                 xMin=np.min(all_x), xMax=np.max(all_x))
            for line in self.lines:
                self.plot.plot([line[0][0], line[1][0]], [line[0][1], line[1][1]], pen=self.all_pen)
                self.cohen_sutherland(line)
            rect = pg.QtGui.QGraphicsRectItem(self.rectangle[0], self.rectangle[2],
                                              abs(self.rectangle[0] - self.rectangle[1]),
                                              abs(self.rectangle[2] - self.rectangle[3]))
            rect.setPen(get_transit_pen())
            self.plot.addItem(rect)

    def cohen_sutherland(self, line):
        # инициализация флага
        flag = 1  # общего положения
        t = 1

        # проверка вертикальности и горизонтальности отрезка
        if line[1][0] - line[0][0] == 0:
            flag = -1   # вертикальный отрезок
        else:
            # вычисление наклона
            t = (line[1][1] - line[0][1]) / (line[1][0] - line[0][0])
            if t == 0:
                flag = 0   # горизонтальный

        # для каждой стороны окна
        for i in range(4):
            vis = is_visible(line, self.rectangle)
            if vis == 1:
                self.plot.plot([line[0][0], line[1][0]], [line[0][1], line[1][1]], pen=self.inside_pen)
                return
            elif not vis:
                return

            # проверка пересечения отрезка и стороны окна
            code1 = get_code(line[0], self.rectangle)
            code2 = get_code(line[1], self.rectangle)

            if code1[i] == code2[i]:
                continue

            # проверка нахождения Р1 вне окна; если Р1 внутри окна, то Р2 и Р1 поменять местами
            if not code1[i]:
                line[0], line[1] = line[1], line[0]

            # поиск пересечений отрезка со сторонами окна
            # контроль вертикальности отрезка
            if flag != -1:
                if i < 2:
                    line[0][1] = t * (self.rectangle[i] - line[0][0]) + line[0][1]
                    line[0][0] = self.rectangle[i]
                    continue
                else:
                    line[0][0] = (1 / t) * (self.rectangle[i] - line[0][1]) + line[0][0]

            line[0][1] = self.rectangle[i]
        self.plot.plot([line[0][0], line[1][0]], [line[0][1], line[1][1]], pen=self.inside_pen)


def main():
    app = QApplication(sys.argv)
    GUI = MainWindow()
    GUI.show()
    sys.exit(app.exec_())


main()
